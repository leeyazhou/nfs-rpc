/**
 * 
 */
package com.bytesgo.nfs.rpc.core.server;

import java.util.concurrent.ExecutorService;

/**
 * @author leeyazhou
 *
 */
public class ServerConfig {

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

}
