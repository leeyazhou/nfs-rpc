/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
package com.bytesgo.nfs.rpc.core.client;

import java.util.List;

import com.bytesgo.nfs.rpc.core.message.ResponseMessage;

/**
 * RPC Client Interface
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface Client {

	/**
	 * invoke sync via simple call
	 * 
	 * @param message
	 * @param timeout
	 * @param codecType    serialize/deserialize type
	 * @param protocolType
	 * @return Object
	 * @throws Exception
	 */
	Object invokeSync(Object message, int timeout, int codecType, int protocolType) throws Exception;

	/**
	 * invoke sync via rpc
	 * 
	 * @param invocation   {@link Invocation}
	 * @param timeout      rcp timeout
	 * @param codecType    serialize/deserialize type
	 * @param protocolType
	 * @return Object return response
	 * @throws Exception if some exception,then will be throwed
	 */
	Object invokeSync(Invocation invocation, int timeout, int codecType, int protocolType) throws Exception;

	/**
	 * receive response from server
	 */
	void putResponse(ResponseMessage response) throws Exception;

	/**
	 * receive responses from server
	 */
	void putResponses(List<ResponseMessage> responses) throws Exception;

	/**
	 * server address
	 * 
	 * @return String
	 */
	String getServerIP();

	/**
	 * server port
	 * 
	 * @return int
	 */
	int getServerPort();

	/**
	 * connect timeout
	 * 
	 * @return int
	 */
	int getConnectTimeout();

	/**
	 * get sending bytes size
	 * 
	 * @return long
	 */
	long getSendingBytesSize();

	/**
	 * Get factory
	 */
	ClientFactory getClientFactory();

}
