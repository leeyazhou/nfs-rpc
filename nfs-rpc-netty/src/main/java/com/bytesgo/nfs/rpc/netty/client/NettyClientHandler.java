package com.bytesgo.nfs.rpc.netty.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.message.ResponseMessage;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Netty4 Client Handler
 * 
 * @author <a href="mailto:coderplay@gmail.com">Min Zhou</a>
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<ResponseMessage> {

  private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientHandler.class);

  private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();

  private NettyClientFactory factory;

  private String key;

  private NettyClient client;

  public NettyClientHandler(NettyClientFactory factory, String key) {
    this.factory = factory;
    this.key = key;
  }

  public void setClient(NettyClient client) {
    this.client = client;
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    if (!(cause instanceof IOException)) {
      // only log
      LOGGER.error("catch some exception not IOException", cause);
    }
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    LOGGER.warn("connection closed: " + ctx.channel().remoteAddress());
    factory.removeClient(key, client);
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage response) throws Exception {
    if (isDebugEnabled) {
      LOGGER.debug("receive response list from server: " + ctx.channel().remoteAddress() + ",request is:" + response.getId());
    }
    client.putResponse(response);
  }

}
