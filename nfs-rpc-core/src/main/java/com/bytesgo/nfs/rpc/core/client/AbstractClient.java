package com.bytesgo.nfs.rpc.core.client;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.codec.Codecs;
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

	@Override
	public Object invokeSync(Object message, int timeout, int codecType, int protocolType) throws Exception {
		RequestMessage wrapper = new RequestMessage(message, timeout, codecType, protocolType);
		return invokeSyncIntern(wrapper);
	}

	@Override
	public Object invokeSync(Invocation invocation, int timeout, int codecType, int protocolType) throws Exception {
		byte[][] argTypeBytes = new byte[invocation.getArgTypes().length][];
		for (int i = 0; i < invocation.getArgTypes().length; i++) {
			argTypeBytes[i] = invocation.getArgTypes()[i].getBytes();
		}
		RequestMessage message = new RequestMessage(invocation.getProcessorName().getBytes(),
				invocation.getMethodName().getBytes(), argTypeBytes, invocation.getArgs(), timeout, codecType,
				protocolType);
		return invokeSyncIntern(message);
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
		} catch (Exception e) {
			responseCache.remove(message.getId());
			rpcResult = null;
			LOGGER.error("send request to os sendbuffer error", e);
			throw e;
		}
		Object result = null;
		try {
			result = rpcResult.getResult(message.getTimeout() - (System.currentTimeMillis() - beginTime),
					TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			responseCache.remove(message.getId());
			LOGGER.error("Get response error", e);
			throw new Exception("Get response error", e);
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
			String errorMsg = "receive response timeout(" + message.getTimeout() + " ms),server is: " + getServerIP()
					+ ":" + getServerPort() + " request id is:" + message.getId();
			throw new Exception(errorMsg);
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
			throw new Exception("only receive ResponseWrapper or List as response");
		}
		try {
			// do deserialize in business threadpool
			if (responseWrapper.getResponse() instanceof byte[]) {
				String responseClassName = null;
				if (responseWrapper.getResponseClassName() != null) {
					responseClassName = new String(responseWrapper.getResponseClassName());
				}
				// avoid server no return object
				if (((byte[]) responseWrapper.getResponse()).length == 0) {
					responseWrapper.setResponse(null);
				} else {
					Object responseObject = Codecs.getDecoder(responseWrapper.getCodecType()).decode(responseClassName,
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
			throw new Exception("Deserialize response object error", e);
		}
		if (responseWrapper.isError()) {
			Throwable t = responseWrapper.getException();
			t.fillInStackTrace();
			String errorMsg = "server error,server is: " + getServerIP() + ":" + getServerPort() + " request id is:"
					+ message.getId();
			LOGGER.error(errorMsg, t);
			throw new Exception(errorMsg, t);
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
