package com.bytesgo.nfs.rpc.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.client.AbstractClientFactory;
import com.bytesgo.nfs.rpc.core.client.Client;
import com.bytesgo.nfs.rpc.netty.protocol.NettyProtocolDecoder;
import com.bytesgo.nfs.rpc.netty.protocol.NettyProtocolEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty4 Client Factory,to create client based on netty API
 * 
 * @author <a href="mailto:coderplay@gmail.com">Min Zhou</a>
 */
public class NettyClientFactory extends AbstractClientFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientFactory.class);

  private static AbstractClientFactory _self = new NettyClientFactory();

  private NettyClientFactory() {
  }

  public static AbstractClientFactory getInstance() {
    return _self;
  }

  protected Client createClient(String targetIP, int targetPort, int connectTimeout, String key) throws Exception {
    final NettyClientHandler handler = new NettyClientHandler(this, key);

    EventLoopGroup group = new NioEventLoopGroup(1);
    Bootstrap b = new Bootstrap();
    b.group(group).channel(NioSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY, Boolean.parseBoolean(System.getProperty("nfs.rpc.tcp.nodelay", "true")))
        .option(ChannelOption.SO_REUSEADDR, Boolean.parseBoolean(System.getProperty("nfs.rpc.tcp.reuseaddress", "true")))
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout < 1000 ? 1000 : connectTimeout)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast("decoder", new NettyProtocolDecoder());
            ch.pipeline().addLast("encoder", new NettyProtocolEncoder());
            ch.pipeline().addLast("handler", handler);
          }
        });

    ChannelFuture future = b.connect(targetIP, targetPort);

    future.awaitUninterruptibly(connectTimeout);
    if (!future.isDone()) {
      LOGGER.error("Create connection to " + targetIP + ":" + targetPort + " timeout!");
      throw new Exception("Create connection to " + targetIP + ":" + targetPort + " timeout!");
    }
    if (future.isCancelled()) {
      LOGGER.error("Create connection to " + targetIP + ":" + targetPort + " cancelled by user!");
      throw new Exception("Create connection to " + targetIP + ":" + targetPort + " cancelled by user!");
    }
    if (!future.isSuccess()) {
      LOGGER.error("Create connection to " + targetIP + ":" + targetPort + " error", future.cause());
      throw new Exception("Create connection to " + targetIP + ":" + targetPort + " error", future.cause());
    }
    NettyClient client = new NettyClient(future, key, connectTimeout);
    handler.setClient(client);
    return client;
  }

}
