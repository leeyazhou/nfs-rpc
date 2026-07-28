package com.bytesgo.nfs.rpc.core.client;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.codec.Codecs;
import com.bytesgo.nfs.rpc.core.exception.RpcDecodeException;
import com.bytesgo.nfs.rpc.core.exception.RpcRejectException;
import com.bytesgo.nfs.rpc.core.exception.RpcRemoteException;
import com.bytesgo.nfs.rpc.core.exception.RpcSendException;
import com.bytesgo.nfs.rpc.core.exception.RpcTimeoutException;
import com.bytesgo.nfs.rpc.core.message.RequestMessage;
import com.bytesgo.nfs.rpc.core.message.ResponseMessage;

/**
 * Common Client,support sync invoke
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public abstract class AbstractClient implements Client {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractClient.class);

	private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();

	private static final boolean isWarnEnabled = LOGGER.isWarnEnabled();

	private static final long PRINT_CONSUME_MINTIME = Long
			.parseLong(System.getProperty("nfs.rpc.print.consumetime", "0"));

	protected static ConcurrentHashMap<Integer, RpcResult> responseCache = new ConcurrentHashMap<Integer, RpcResult>();

	private static final ExecutorService ASYNC_EXECUTOR = Executors.newCachedThreadPool(new ThreadFactory() {
		private final AtomicInteger counter = new AtomicInteger(0);

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "nfs-rpc-async-" + counter.incrementAndGet());
			t.setDaemon(true);
			return t;
		}
	});

	@Override
	public Object invokeSync(Object message, int timeout, int codecType, int protocolType) throws Exception {
		RequestMessage wrapper = new RequestMessage(message, timeout, codecType, protocolType);
		return invokeSyncIntern(wrapper);
	}

	@Override
	public Object invokeSync(Invocation invocation, int timeout, int codecType, int protocolType) throws Exception {
		byte[][] argTypeBytes = new byte[invocation.getArgTypes().length][];
		for (int i = 0; i < invocation.getArgTypes().length; i++) {
			argTypeBytes[i] = invocation.getArgTypes()[i].getBytes(StandardCharsets.UTF_8);
		}
		RequestMessage message = new RequestMessage(invocation.getProcessorName().getBytes(StandardCharsets.UTF_8),
				invocation.getMethodName().getBytes(StandardCharsets.UTF_8), argTypeBytes, invocation.getArgs(), timeout, codecType,
				protocolType);
		return invokeSyncIntern(message);
	}

	@Override
	public CompletableFuture<Object> invokeAsync(Object message, int timeout, int codecType, int protocolType) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return invokeSync(message, timeout, codecType, protocolType);
			} catch (Exception e) {
				throw new CompletionException(e);
			}
		}, ASYNC_EXECUTOR);
	}

	@Override
	public CompletableFuture<Object> invokeAsync(Invocation invocation, int timeout, int codecType, int protocolType) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return invokeSync(invocation, timeout, codecType, protocolType);
			} catch (Exception e) {
				throw new CompletionException(e);
			}
		}, ASYNC_EXECUTOR);
	}

	private Object invokeSyncIntern(RequestMessage message) throws Exception {
		long beginTime = System.currentTimeMillis();
		RpcResult rpcResult = new RpcResult();
		responseCache.put(message.getId(), rpcResult);
		ResponseMessage responseWrapper = null;
		try {
			if (isDebugEnabled) {
				// for performance trace
				LOGGER.debug("client ready to send message,request id: " + message.getId());
			}
			getClientFactory().checkSendLimit();
			sendRequest(message, message.getTimeout());
			if (isDebugEnabled) {
				// for performance trace
				LOGGER.debug("client write message to send buffer,wait for response,request id: " + message.getId());
			}
		} catch (RpcRejectException e) {
			responseCache.remove(message.getId());
			throw e;
		} catch (Exception e) {
			responseCache.remove(message.getId());
			LOGGER.error("send request to os sendbuffer error", e);
			throw new RpcSendException("send request to os sendbuffer error", e);
		}
		Object result = null;
		try {
			result = rpcResult.getResult(message.getTimeout() - (System.currentTimeMillis() - beginTime),
					TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			responseCache.remove(message.getId());
			LOGGER.error("Get response error", e);
			throw new RpcDecodeException("Get response error", e);
		}
		responseCache.remove(message.getId());

		if (PRINT_CONSUME_MINTIME > 0 && isWarnEnabled) {
			long consumeTime = System.currentTimeMillis() - beginTime;
			if (consumeTime > PRINT_CONSUME_MINTIME) {
				LOGGER.warn("client.invokeSync consume time: " + consumeTime + " ms, server is: " + getServerIP() + ":"
						+ getServerPort() + " request id is:" + message.getId());
			}
		}
		if (result == null) {
			throw new RpcTimeoutException(message.getTimeout(), getServerIP() + ":" + getServerPort(), message.getId());
		}

		if (result instanceof ResponseMessage) {
			responseWrapper = (ResponseMessage) result;
		} else if (result instanceof List) {
			@SuppressWarnings("unchecked")
			List<ResponseMessage> responseMessages = (List<ResponseMessage>) result;
			for (ResponseMessage response : responseMessages) {
				if (response.getId() == message.getId()) {
					responseWrapper = response;
				} else {
					putResponse(response);
				}
			}
		} else {
			throw new RpcDecodeException("only receive ResponseMessage or List as response");
		}
		try {
			// do deserialize in business threadpool
			if (responseWrapper.getResponse() instanceof byte[]) {
				String responseClassName = null;
				if (responseWrapper.getResponseClassName() != null) {
					responseClassName = new String(responseWrapper.getResponseClassName(), StandardCharsets.UTF_8);
				}
				// avoid server no return object
				if (((byte[]) responseWrapper.getResponse()).length == 0) {
					responseWrapper.setResponse(null);
				} else {
					Object responseObject = Codecs.getCodec(responseWrapper.getCodecType()).decode(responseClassName,
							(byte[]) responseWrapper.getResponse());
					if (responseObject instanceof Throwable) {
						responseWrapper.setException((Throwable) responseObject);
					} else {
						responseWrapper.setResponse(responseObject);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Deserialize response object error", e);
			throw new RpcDecodeException("Deserialize response object error", e);
		}
		if (responseWrapper.isError()) {
			Throwable t = responseWrapper.getException();
			t.fillInStackTrace();
			String errorMsg = "server error,server is: " + getServerIP() + ":" + getServerPort() + " request id is:"
					+ message.getId();
			LOGGER.error(errorMsg, t);
			throw new RpcRemoteException(errorMsg, t, getServerIP() + ":" + getServerPort(), message.getId());
		}
		return responseWrapper.getResponse();
	}

	/**
	 * receive response
	 */
	public void putResponse(ResponseMessage wrapper) throws Exception {
		if (!responseCache.containsKey(wrapper.getId())) {
			LOGGER.warn("give up the response,request id is:" + wrapper.getId() + ",maybe because timeout!");
			return;
		}
		RpcResult rpcResult = responseCache.get(wrapper.getId());
		if (rpcResult != null) {
			rpcResult.setResult(wrapper);
		} else {
			LOGGER.warn("give up the response,request id is:" + wrapper.getId() + ",because queue is null");
		}
	}

	/**
	 * receive responses
	 */
	public void putResponses(List<ResponseMessage> wrappers) throws Exception {
		for (ResponseMessage wrapper : wrappers) {
			if (!responseCache.containsKey(wrapper.getId())) {
				LOGGER.warn("give up the response,request id is:" + wrapper.getId() + ",maybe because timeout!");
				continue;
			}
			RpcResult rpcResult = responseCache.get(wrapper.getId());
			if (rpcResult != null) {
				rpcResult.setResult(wrapper);
				break;
			} else {
				LOGGER.warn("give up the response,request id is:" + wrapper.getId() + ",because queue is null");
			}
		}
	}

	/**
	 * send request to os sendbuffer,must ensure write result
	 */
	public abstract void sendRequest(RequestMessage message, int timeout) throws Exception;

}
