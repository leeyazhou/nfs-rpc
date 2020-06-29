package com.bytesgo.nfs.rpc.grizzly.server;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.EmptyCompletionHandler;
import org.glassfish.grizzly.WriteResult;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.message.RequestMessage;
import com.bytesgo.nfs.rpc.core.message.ResponseMessage;
import com.bytesgo.nfs.rpc.core.protocol.ProtocolFactory;

/**
 * Grizzly Server Handler
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class GrizzlyServerHandler extends BaseFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(GrizzlyServerHandler.class);

  private final ExecutorService threadPool;

  public GrizzlyServerHandler(final ExecutorService threadPool) {
    this.threadPool = threadPool;
  }

  public NextAction handleRead(final FilterChainContext ctx) throws IOException {
    final Object message = ctx.getMessage();
    final Connection<?> connection = ctx.getConnection();

    try {
      threadPool.execute(new HandlerRunnable(connection, message, threadPool));
    } catch (RejectedExecutionException exception) {
      LOGGER.error("server threadpool full,threadpool maxsize is:" + ((ThreadPoolExecutor) threadPool).getMaximumPoolSize());
      if (message instanceof List) {
        @SuppressWarnings("unchecked")
        List<RequestMessage> requests = (List<RequestMessage>) message;
        for (final RequestMessage request : requests) {
          sendErrorResponse(connection, request);
        }
      } else {
        sendErrorResponse(connection, (RequestMessage) message);
      }
    }

    return ctx.getStopAction();
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private void sendErrorResponse(final Connection connection, final RequestMessage request) throws IOException {
    ResponseMessage responseWrapper = new ResponseMessage(request.getId(), request.getCodecType(), request.getProtocolType());
    responseWrapper.setException(new Exception("server threadpool full,maybe because server is slow or too many requests"));

    connection.write(responseWrapper, new EmptyCompletionHandler<WriteResult>() {

      @Override
      public void failed(Throwable throwable) {
        LOGGER.error("server write response error,request id is: " + request.getId());
      }

    });
  }

  @SuppressWarnings("rawtypes")
  class HandlerRunnable implements Runnable {

    private final Connection connection;
    private final Object message;
    private final ExecutorService threadPool;

    public HandlerRunnable(Connection connection, Object message, ExecutorService threadPool) {
      this.connection = connection;
      this.message = message;
      this.threadPool = threadPool;
    }

    @SuppressWarnings({ "unchecked" })
    public void run() {
      // pipeline
      if (message instanceof List) {
        List messages = (List) message;
        for (Object messageObject : messages) {
          threadPool.execute(new HandlerRunnable(connection, messageObject, threadPool));
        }
      } else {
        RequestMessage request = (RequestMessage) message;
        long beginTime = System.currentTimeMillis();
        ResponseMessage responseWrapper = ProtocolFactory.getServerHandler(request.getProtocolType()).handleRequest(request);
        final int id = request.getId();
        // already timeout,so not return
        if ((System.currentTimeMillis() - beginTime) >= request.getTimeout()) {
          LOGGER.warn("timeout,so give up send response to client,requestId is:" + id + ",client is:" + connection.getPeerAddress()
              + ",consumetime is:" + (System.currentTimeMillis() - beginTime) + ",timeout is:" + request.getTimeout());
          return;
        }

        connection.write(responseWrapper, new EmptyCompletionHandler<WriteResult>() {
          @Override
          public void failed(Throwable throwable) {
            LOGGER.error("server write response error,request id is: " + id);
          }
        });
      }
    }
  }

}
