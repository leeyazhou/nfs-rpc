package com.bytesgo.nfs.rpc.core.server;

/**
 * RPC Server Interface
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface Server {

	/**
	 * start server at listenPort,requests will be handled in businessThreadPool
	 */
	void start() throws Exception;

	/**
	 * register business handler
	 */
	void registerProcessor(int protocolType, String serviceName, Object serviceInstance);

	/**
	 * stop server
	 */
	void stop() throws Exception;

}
