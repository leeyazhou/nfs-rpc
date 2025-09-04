/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
package com.bytesgo.nfs.rpc.core.protocol;

/**
 * Protocol Interface,for custom network protocol
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface Protocol {

	/**
	 * encode Message to byte & write to network framework
	 * 
	 * @param message
	 * @param byteBuffer
	 * @throws Exception
	 */
	ByteBufferWrapper encode(Object message, ByteBufferWrapper bytebuffer) throws ProtocolException;

	/**
	 * decode stream to object
	 * 
	 * @param wrapper
	 * @param errorObject stream not enough,then return this object
	 * @return Object
	 * @throws Exception
	 */
	Object decode(ByteBufferWrapper wrapper, Object errorObject, int... originPos) throws ProtocolException;

}