package com.bytesgo.nfs.rpc.grizzly.server;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.strategies.SameThreadIOStrategy;
import org.glassfish.grizzly.threadpool.GrizzlyExecutorService;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.protocol.ProtocolFactory;
import com.bytesgo.nfs.rpc.core.server.Server;
import com.bytesgo.nfs.rpc.core.server.ServerConfig;
import com.bytesgo.nfs.rpc.grizzly.protocol.GrizzlyProtocolFilter;

/**
 * Grizzly Server
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class GrizzlyServer implements Server {

	private static final Logger LOGGER = LoggerFactory.getLogger(GrizzlyServer.class);
	private final AtomicBoolean startFlag = new AtomicBoolean(false);
	private TCPNIOTransport transport = null;
	private ServerConfig serverConfig;

	public GrizzlyServer(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	public void start() throws Exception {
		if (!startFlag.compareAndSet(false, true)) {
			return;
		}
		serverConfig.validate();
		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) serverConfig.getBusinessThreadPool();
		ThreadPoolConfig config = ThreadPoolConfig.defaultConfig().copy()
				.setCorePoolSize(threadPoolExecutor.getCorePoolSize())
				.setMaxPoolSize(threadPoolExecutor.getMaximumPoolSize()).setPoolName("GRIZZLY-SERVER");
		ExecutorService executorService = GrizzlyExecutorService.createInstance(config);

		FilterChainBuilder filterChainBuilder = FilterChainBuilder.stateless();
		filterChainBuilder.add(new TransportFilter());
		filterChainBuilder.add(new GrizzlyProtocolFilter());
		filterChainBuilder.add(new GrizzlyServerHandler(executorService));
		TCPNIOTransportBuilder builder = TCPNIOTransportBuilder.newInstance();
		builder.setOptimizedForMultiplexing(true);
		builder.setIOStrategy(SameThreadIOStrategy.getInstance());

		transport = builder.build();

		transport.setProcessor(filterChainBuilder.build());
		transport.bind(serverConfig.getHost(), serverConfig.getPort());

		transport.start();
		LOGGER.info("server started,listen at: " + serverConfig.getPort());
	}

	public void stop() throws Exception {
		if (!startFlag.compareAndSet(true, false)) {
			return;
		}
		if (transport != null) {
			LOGGER.warn("Server stop begin, shutting down...");
			transport.shutdown(5, TimeUnit.SECONDS);
			LOGGER.warn("Server stopped.");
		}
	}

	public void registerProcessor(int protocolType, String serviceName, Object serviceInstance) {
		ProtocolFactory.getServerHandler(protocolType).registerProcessor(serviceName, serviceInstance);
	}
}
