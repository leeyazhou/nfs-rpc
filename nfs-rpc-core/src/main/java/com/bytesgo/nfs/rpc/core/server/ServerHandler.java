package com.bytesgo.nfs.rpc.core.server;

import com.bytesgo.nfs.rpc.core.RequestWrapper;
import com.bytesgo.nfs.rpc.core.ResponseWrapper;

/**
 * Server Handler interface,when server receive message,it will handle
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface ServerHandler {

	/**
	 * register business handler,provide for Server
	 */
	void registerProcessor(String instanceName, Object instance);

	/**
	 * handle the request
	 */
	ResponseWrapper handleRequest(final RequestWrapper request);

}