package com.bytesgo.nfs.rpc.mina.server;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.protocol.ProtocolFactory;
import com.bytesgo.nfs.rpc.core.server.Server;
import com.bytesgo.nfs.rpc.core.server.ServerConfig;
import com.bytesgo.nfs.rpc.core.util.concurrent.NamedThreadFactory;
import com.bytesgo.nfs.rpc.mina.protocol.MinaProtocolCodecFilter;

/**
 * Mina Server
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaServer implements Server {

	private static final Logger LOGGER = LoggerFactory.getLogger(MinaServer.class);

	private static final String ACCEPTOR_THREADNAME = "MINASERVER-ACCEPTOR";

	private IoAcceptor acceptor;

	private AtomicBoolean startFlag = new AtomicBoolean();

	private MinaServerHandler serverHandler = null;
	private ServerConfig serverConfig;

	public MinaServer(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
		acceptor = new SocketAcceptor(Runtime.getRuntime().availableProcessors() + 1,
				Executors.newCachedThreadPool(new NamedThreadFactory(ACCEPTOR_THREADNAME)));
		acceptor.getDefaultConfig().setThreadModel(ThreadModel.MANUAL);
		acceptor.getDefaultConfig().getFilterChain().addLast("objectserialize", new MinaProtocolCodecFilter());
	}

	@Override
	public void start() throws Exception {
		if (!startFlag.compareAndSet(false, true)) {
			return;
		}
		try {
			serverHandler = new MinaServerHandler(serverConfig.getBusinessThreadPool());
			acceptor.bind(new InetSocketAddress(serverConfig.getHost(), serverConfig.getPort()), serverHandler);
			LOGGER.warn("Server started,listen at: " + serverConfig.getPort());
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
		acceptor.unbindAll();
		startFlag.set(false);
	}
}
