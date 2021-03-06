package com.bytesgo.nfs.rpc.netty4.server;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.protocol.ProtocolFactory;
import com.bytesgo.nfs.rpc.core.server.Server;
import com.bytesgo.nfs.rpc.core.server.ServerConfig;
import com.bytesgo.nfs.rpc.netty4.protocol.Netty4ProtocolDecoder;
import com.bytesgo.nfs.rpc.netty4.protocol.Netty4ProtocolEncoder;

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
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Netty4 Server
 * 
 * @author <a href="mailto:coderplay@gmail.com">Min Zhou</a>
 */
public class Netty4Server implements Server {

	private static final Logger LOGGER = LoggerFactory.getLogger(Netty4Server.class);
	private AtomicBoolean startFlag = new AtomicBoolean(false);

	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup ioGroup;
	private EventExecutorGroup businessGroup;
	private ServerConfig serverConfig;

	public Netty4Server(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	@Override
	public void start() throws Exception {
		if (!startFlag.compareAndSet(false, true)) {
			return;
		}
		bossGroup = new NioEventLoopGroup();
		ioGroup = new NioEventLoopGroup();
		businessGroup = new DefaultEventExecutorGroup(serverConfig.getMaxPoolSize());

		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, ioGroup).channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.TCP_NODELAY,
						Boolean.parseBoolean(System.getProperty("nfs.rpc.tcp.nodelay", "true")))
				.childOption(ChannelOption.SO_REUSEADDR,
						Boolean.parseBoolean(System.getProperty("nfs.rpc.tcp.reuseaddress", "true")))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("decoder", new Netty4ProtocolDecoder());
						ch.pipeline().addLast("encoder", new Netty4ProtocolEncoder());
						ch.pipeline().addLast(businessGroup, "handler", new Netty4ServerHandler());
					}
				});
		b.bind(new InetSocketAddress(serverConfig.getHost(), serverConfig.getPort())).sync();
		LOGGER.info("Server started,listen at: " + serverConfig.getPort() + ", businessThreads is "
				+ serverConfig.getMaxPoolSize());
	}

	public void registerProcessor(int protocolType, String serviceName, Object serviceInstance) {
		ProtocolFactory.getServerHandler(protocolType).registerProcessor(serviceName, serviceInstance);
	}

	public void stop() throws Exception {
		LOGGER.warn("Server stop!");
		bossGroup.shutdownGracefully();
		ioGroup.shutdownGracefully();
		businessGroup.shutdownGracefully();
		startFlag.set(false);
	}

}
