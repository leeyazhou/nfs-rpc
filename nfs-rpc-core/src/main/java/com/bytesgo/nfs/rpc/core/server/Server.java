package com.bytesgo.nfs.rpc.core.server;

/**
 * RPC Server Interface
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface Server {

	/**
	 * start server at listenPort,requests will be handled in businessThreadPool
	 * 
	 * @throws Exception Exception
	 */
	void start() throws Exception;

	/**
	 * register business handler
	 * 
	 * @param protocolType    protocolType
	 * @param serviceName     serviceName
	 * @param serviceInstance serviceInstance
	 */
	void registerProcessor(int protocolType, String serviceName, Object serviceInstance);

	/**
	 * stop server
	 * 
	 * @throws Exception Exception
	 */
	void stop() throws Exception;

}
