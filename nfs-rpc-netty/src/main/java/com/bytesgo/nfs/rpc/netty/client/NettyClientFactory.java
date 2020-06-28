package com.bytesgo.nfs.rpc.netty.client;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.NamedThreadFactory;
import com.bytesgo.nfs.rpc.core.client.AbstractClientFactory;
import com.bytesgo.nfs.rpc.core.client.Client;

/**
 * Netty Client Factory,to create client based on netty API
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettyClientFactory extends AbstractClientFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientFactory.class);

  private static AbstractClientFactory _self = new NettyClientFactory();

  private static final ThreadFactory bossThreadFactory = new NamedThreadFactory("NETTYCLIENT-BOSS-");

  private static final ThreadFactory workerThreadFactory = new NamedThreadFactory("NETTYCLIENT-WORKER-");

  private static NioClientSocketChannelFactory nioClient = new NioClientSocketChannelFactory(
      Executors.newCachedThreadPool(bossThreadFactory), Executors.newCachedThreadPool(workerThreadFactory));

  private NettyClientFactory() {
    ;
  }

  public static AbstractClientFactory getInstance() {
    return _self;
  }

  protected Client createClient(String targetIP, int targetPort, int connectTimeout, String key) throws Exception {
    ClientBootstrap bootstrap = new ClientBootstrap(nioClient);
    bootstrap.setOption("tcpNoDelay", Boolean.parseBoolean(System.getProperty("nfs.rpc.tcp.nodelay", "true")));
    bootstrap.setOption("reuseAddress", Boolean.parseBoolean(System.getProperty("nfs.rpc.tcp.reuseaddress", "true")));
    if (connectTimeout < 1000) {
      bootstrap.setOption("connectTimeoutMillis", 1000);
    } else {
      bootstrap.setOption("connectTimeoutMillis", connectTimeout);
    }
    NettyClientHandler handler = new NettyClientHandler(this, key);
    bootstrap.setPipelineFactory(new NettyClientPipelineFactory(handler));
    ChannelFuture future = bootstrap.connect(new InetSocketAddress(targetIP, targetPort));
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
      LOGGER.error("Create connection to " + targetIP + ":" + targetPort + " error", future.getCause());
      throw new Exception("Create connection to " + targetIP + ":" + targetPort + " error", future.getCause());
    }
    NettyClient client = new NettyClient(future, key, connectTimeout);
    handler.setClient(client);
    return client;
  }

}
