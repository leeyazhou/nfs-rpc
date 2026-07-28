package com.bytesgo.nfs.rpc.netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.protocol.ProtocolFactory;
import com.bytesgo.nfs.rpc.core.server.Server;
import com.bytesgo.nfs.rpc.core.server.ServerConfig;
import com.bytesgo.nfs.rpc.netty.protocol.NettyProtocolDecoder;
import com.bytesgo.nfs.rpc.netty.protocol.NettyProtocolEncoder;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Netty4 Server
 * 
 * @author <a href="mailto:coderplay@gmail.com">Min Zhou</a>
 */
public class NettyServer implements Server {

	private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);
	private static final int DEFAULT_IDLE_TIMEOUT = Integer.getInteger("nfs.rpc.server.idle.timeout", 120);
	private final AtomicBoolean startFlag = new AtomicBoolean(false);

	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup ioGroup;
	private EventExecutorGroup businessGroup;
	private ServerConfig serverConfig;

	public NettyServer(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	@Override
	public void start() throws Exception {
		if (!startFlag.compareAndSet(false, true)) {
			return;
		}
		serverConfig.validate();
		bossGroup = new NioEventLoopGroup();
		ioGroup = new NioEventLoopGroup();
		businessGroup = new DefaultEventExecutorGroup(serverConfig.getMaxPoolSize());

		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, ioGroup).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG,
						Integer.parseInt(System.getProperty("nfs.rpc.server.backlog", "1024")))
				.childOption(ChannelOption.TCP_NODELAY,
						Boolean.parseBoolean(System.getProperty("nfs.rpc.tcp.nodelay", "true")))
				.childOption(ChannelOption.SO_REUSEADDR,
						Boolean.parseBoolean(System.getProperty("nfs.rpc.tcp.reuseaddress", "true")))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("idle", new IdleStateHandler(DEFAULT_IDLE_TIMEOUT, 0, 0));
						ch.pipeline().addLast("decoder", new NettyProtocolDecoder());
						ch.pipeline().addLast("encoder", new NettyProtocolEncoder());
						ch.pipeline().addLast(businessGroup, "handler", new NettyServerHandler());
					}
				});
		b.bind(new InetSocketAddress(serverConfig.getHost(), serverConfig.getPort())).sync();
		LOGGER.info("Server started,listen at: " + serverConfig.getPort() + ", businessThreads is "
				+ serverConfig.getMaxPoolSize() + ", idleTimeout is " + DEFAULT_IDLE_TIMEOUT + "s");
	}

	public void registerProcessor(int protocolType, String serviceName, Object serviceInstance) {
		ProtocolFactory.getServerHandler(protocolType).registerProcessor(serviceName, serviceInstance);
	}

	public void stop() throws Exception {
		if (!startFlag.compareAndSet(true, false)) {
			return;
		}
		LOGGER.warn("Server stop begin, draining connections...");
		bossGroup.shutdownGracefully(0, 5, java.util.concurrent.TimeUnit.SECONDS);
		ioGroup.shutdownGracefully(0, 5, java.util.concurrent.TimeUnit.SECONDS);
		businessGroup.shutdownGracefully(0, 5, java.util.concurrent.TimeUnit.SECONDS);
		LOGGER.warn("Server stopped.");
	}

}
