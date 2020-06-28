package com.bytesgo.nfs.rpc.netty.server;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.common.NamedThreadFactory;
import com.bytesgo.nfs.rpc.common.ProtocolFactory;
import com.bytesgo.nfs.rpc.common.server.Server;
import com.bytesgo.nfs.rpc.netty.protocol.NettyProtocolDecoder;
import com.bytesgo.nfs.rpc.netty.protocol.NettyProtocolEncoder;

/**
 * Netty Server
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettyServer implements Server {

  private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

  private ServerBootstrap bootstrap = null;

  private AtomicBoolean startFlag = new AtomicBoolean(false);

  public NettyServer() {
    ThreadFactory serverBossTF = new NamedThreadFactory("NETTYSERVER-BOSS-");
    ThreadFactory serverWorkerTF = new NamedThreadFactory("NETTYSERVER-WORKER-");
    bootstrap = new ServerBootstrap(
        new NioServerSocketChannelFactory(Executors.newCachedThreadPool(serverBossTF), Executors.newCachedThreadPool(serverWorkerTF)));
    bootstrap.setOption("tcpNoDelay", Boolean.parseBoolean(System.getProperty("nfs.rpc.tcp.nodelay", "true")));
    bootstrap.setOption("reuseAddress", Boolean.parseBoolean(System.getProperty("nfs.rpc.tcp.reuseaddress", "true")));
  }

  public void start(int listenPort, final ExecutorService threadPool) throws Exception {
    if (!startFlag.compareAndSet(false, true)) {
      return;
    }
    bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
      public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = new DefaultChannelPipeline();
        pipeline.addLast("decoder", new NettyProtocolDecoder());
        pipeline.addLast("encoder", new NettyProtocolEncoder());
        pipeline.addLast("handler", new NettyServerHandler(threadPool));
        return pipeline;
      }
    });
    bootstrap.bind(new InetSocketAddress(listenPort));
    LOGGER.warn("Server started,listen at: " + listenPort);
  }

  public void registerProcessor(int protocolType, String serviceName, Object serviceInstance) {
    ProtocolFactory.getServerHandler(protocolType).registerProcessor(serviceName, serviceInstance);
  }

  public void stop() throws Exception {
    LOGGER.warn("Server stop!");
    bootstrap.releaseExternalResources();
    startFlag.set(false);
  }

}
