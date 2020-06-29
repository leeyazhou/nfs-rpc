package com.bytesgo.nfs.rpc.core.server;

import com.bytesgo.nfs.rpc.core.message.RequestMessage;
import com.bytesgo.nfs.rpc.core.message.ResponseMessage;

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
	ResponseMessage handleRequest(final RequestMessage request);

}