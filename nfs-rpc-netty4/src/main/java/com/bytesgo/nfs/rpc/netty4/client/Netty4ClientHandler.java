package com.bytesgo.nfs.rpc.netty4.client;

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
public class Netty4ClientHandler extends SimpleChannelInboundHandler<ResponseMessage> {

  private static final Logger LOGGER = LoggerFactory.getLogger(Netty4ClientHandler.class);

  private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();

  private Netty4ClientFactory factory;

  private String key;

  private Netty4Client client;

  public Netty4ClientHandler(Netty4ClientFactory factory, String key) {
    this.factory = factory;
    this.key = key;
  }

  public void setClient(Netty4Client client) {
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
