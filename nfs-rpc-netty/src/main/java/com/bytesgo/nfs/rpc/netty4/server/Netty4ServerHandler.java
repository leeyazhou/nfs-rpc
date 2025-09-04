package com.bytesgo.nfs.rpc.netty4.server;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.message.RequestMessage;
import com.bytesgo.nfs.rpc.core.message.ResponseMessage;
import com.bytesgo.nfs.rpc.core.protocol.ProtocolFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Netty4 Server Handler
 * 
 * @author <a href="mailto:coderplay@gmail.com">Min Zhou</a>
 */
public class Netty4ServerHandler extends SimpleChannelInboundHandler<RequestMessage> {

  private static final Logger LOGGER = LoggerFactory.getLogger(Netty4ServerHandler.class);

  /**
   * Calls {@link ChannelHandlerContext#fireExceptionCaught(Throwable)} to
   * forward to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
   *
   * Sub-classes may override this method to change behavior.
   */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    if (!(cause instanceof IOException)) {
      // only log
      LOGGER.error("catch some exception not IOException", cause);
    }
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RequestMessage request) throws Exception {
    long beginTime = System.currentTimeMillis();
    ResponseMessage responseWrapper = ProtocolFactory.getServerHandler(request.getProtocolType()).handleRequest(request);
    final int id = request.getId();
    // already timeout,so not return
    if ((System.currentTimeMillis() - beginTime) >= request.getTimeout()) {
      LOGGER.warn("timeout,so give up send response to client,requestId is:" + id + ",client is:" + ctx.channel().remoteAddress()
          + ",consumetime is:" + (System.currentTimeMillis() - beginTime) + ",timeout is:" + request.getTimeout());
      return;
    }
    ChannelFuture wf = ctx.channel().writeAndFlush(responseWrapper);
    wf.addListener(new ChannelFutureListener() {
      public void operationComplete(ChannelFuture future) throws Exception {
        if (!future.isSuccess()) {
          LOGGER.error("server write response error,request id is: " + id);
        }
      }
    });
  }

}
