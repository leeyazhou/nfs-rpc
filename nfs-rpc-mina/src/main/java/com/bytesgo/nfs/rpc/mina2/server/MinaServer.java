package com.bytesgo.nfs.rpc.mina2.server;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.protocol.ProtocolFactory;
import com.bytesgo.nfs.rpc.core.server.Server;
import com.bytesgo.nfs.rpc.core.server.ServerConfig;
import com.bytesgo.nfs.rpc.mina2.protocol.MinaProtocolCodecFilter;

/**
 * Mina2 Server
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaServer implements Server {

	private static final Logger LOGGER = LoggerFactory.getLogger(MinaServer.class);

	private IoAcceptor acceptor;

	private AtomicBoolean startFlag = new AtomicBoolean();

	private MinaServerHandler serverHandler = null;
	private ServerConfig serverConfig;

	public MinaServer(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
		acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
		((NioSocketAcceptor) acceptor).setReuseAddress(true);
		((NioSocketAcceptor) acceptor).getSessionConfig().setTcpNoDelay(true);
		// acceptor.getFilterChain().addLast("executor", new ExecutorFilter(5,
		// 10));
		acceptor.getFilterChain().addLast("objectserialize", new MinaProtocolCodecFilter());
	}

	@Override
	public void start() throws Exception {
		if (!startFlag.compareAndSet(false, true)) {
			return;
		}
		try {
			serverHandler = new MinaServerHandler(serverConfig.getBusinessThreadPool());
			acceptor.setHandler(serverHandler);
			acceptor.bind(new InetSocketAddress(serverConfig.getHost(), serverConfig.getPort()));
			LOGGER.info("Server started,listen at: " + serverConfig.getPort());
		} catch (Exception e) {
			startFlag.set(false);
			LOGGER.error("Server start failed", e);
			throw new Exception("start server error", e);
		}
	}

	public void registerProcessor(int protocolType, String serviceName, Object serviceInstance) {
		ProtocolFactory.getServerHandler(protocolType).registerProcessor(serviceName, serviceInstance);
	}

	public void stop() throws Exception {
		serverHandler = null;
		LOGGER.warn("Server stoped");
		acceptor.dispose();
		startFlag.set(false);
	}
}
