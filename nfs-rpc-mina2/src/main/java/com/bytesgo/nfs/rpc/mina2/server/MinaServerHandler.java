package com.bytesgo.nfs.rpc.mina2.server;

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

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.common.ProtocolFactory;
import com.bytesgo.nfs.rpc.common.RequestWrapper;
import com.bytesgo.nfs.rpc.common.ResponseWrapper;

/**
 * Mina Server Handler to receive message,handle exception
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaServerHandler extends IoHandlerAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(MinaServerHandler.class);

  private ExecutorService threadpool;

  public MinaServerHandler(ExecutorService threadpool) {
    this.threadpool = threadpool;
  }

  public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    if (!(cause instanceof IOException)) {
      // only log
      LOGGER.error("catch some exception not IOException,so close session", cause);
    }
  }

  public void messageReceived(final IoSession session, final Object message) throws Exception {
    if (!(message instanceof RequestWrapper) && !(message instanceof List)) {
      LOGGER.error("receive message error,only support RequestWrapper || List");
      throw new Exception("receive message error,only support RequestWrapper || List");
    }
    handleRequest(session, message);
  }

  @SuppressWarnings("unchecked")
  private void handleRequest(final IoSession session, final Object message) {
    try {
      threadpool.execute(new HandlerRunnable(session, message, threadpool));
    } catch (RejectedExecutionException exception) {
      LOGGER.error("server threadpool full,threadpool maxsize is:" + ((ThreadPoolExecutor) threadpool).getMaximumPoolSize());
      if (message instanceof List) {
        List<RequestWrapper> requests = (List<RequestWrapper>) message;
        for (final RequestWrapper request : requests) {
          sendErrorResponse(session, request);
        }
      } else {
        sendErrorResponse(session, (RequestWrapper) message);
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void sendErrorResponse(final IoSession session, final RequestWrapper request) {
    ResponseWrapper responseWrapper = new ResponseWrapper(request.getId(), request.getCodecType(), request.getProtocolType());
    responseWrapper.setException(new Exception("server threadpool full,maybe because server is slow or too many requests"));
    WriteFuture wf = session.write(responseWrapper);
    wf.addListener(new IoFutureListener() {
      public void operationComplete(IoFuture future) {
        if (!((WriteFuture) future).isWritten()) {
          LOGGER.error("server write response error,request id is: " + request.getId());
        }
      }
    });
  }

  class HandlerRunnable implements Runnable {

    private IoSession session;

    private Object message;

    private ExecutorService threadPool;

    public HandlerRunnable(IoSession session, Object message, ExecutorService threadPool) {
      this.session = session;
      this.message = message;
      this.threadPool = threadPool;
    }

    @SuppressWarnings("rawtypes")
    public void run() {
      // pipeline
      if (message instanceof List) {
        List messages = (List) message;
        for (Object messageObject : messages) {
          threadPool.execute(new HandlerRunnable(session, messageObject, threadPool));
        }
      } else {
        RequestWrapper request = (RequestWrapper) message;
        long beginTime = System.currentTimeMillis();
        ResponseWrapper responseWrapper = ProtocolFactory.getServerHandler(request.getProtocolType()).handleRequest(request);
        final int id = request.getId();
        // already timeout,so not return
        if ((System.currentTimeMillis() - beginTime) >= request.getTimeout()) {
          LOGGER.warn("timeout,so give up send response to client,requestId is:" + id + ",client is:" + session.getRemoteAddress()
              + ",consumetime is:" + (System.currentTimeMillis() - beginTime) + ",timeout is:" + request.getTimeout());
          return;
        }
        WriteFuture wf = session.write(responseWrapper);
        wf.addListener(new IoFutureListener() {
          public void operationComplete(IoFuture future) {
            if (!((WriteFuture) future).isWritten()) {
              LOGGER.error("server write response error,request id is: " + id);
            }
          }
        });
      }
    }

  }

}
