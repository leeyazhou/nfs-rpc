/**
 * 
 */
package com.bytesgo.nfs.rpc.core.server;

import java.util.concurrent.ExecutorService;

import com.bytesgo.nfs.rpc.core.exception.NFSException;

/**
 * @author leeyazhou
 *
 */
public class ServerConfig {

	private static final int MIN_PORT = 1;
	private static final int MAX_PORT = 65535;

	private String host;
	private int port;
	private int maxPoolSize;
	private ExecutorService businessThreadPool;

	public String getHost() {
		return host;
	}

	public ServerConfig setHost(String host) {
		this.host = host;
		return this;
	}

	public int getPort() {
		return port;
	}

	public ServerConfig setPort(int port) {
		this.port = port;
		return this;
	}

	public ExecutorService getBusinessThreadPool() {
		return businessThreadPool;
	}

	public ServerConfig setBusinessThreadPool(ExecutorService businessThreadPool) {
		this.businessThreadPool = businessThreadPool;
		return this;
	}

	/**
	 * @param maxPoolSize the maxPoolSize to set
	 * @return ServerConfig
	 */
	public ServerConfig setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
		return this;
	}

	/**
	 * @return the maxPoolSize
	 */
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public ServerConfig validate() {
		if (port < MIN_PORT || port > MAX_PORT) {
			throw new NFSException("Invalid port: " + port + ", must be between " + MIN_PORT + " and " + MAX_PORT);
		}
		if (host != null && host.trim().isEmpty()) {
			throw new NFSException("Host must not be blank if set");
		}
		if (maxPoolSize < 0) {
			throw new NFSException("Invalid maxPoolSize: " + maxPoolSize + ", must be >= 0");
		}
		return this;
	}

}
