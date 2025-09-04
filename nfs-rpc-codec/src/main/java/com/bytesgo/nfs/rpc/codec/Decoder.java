/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
package com.bytesgo.nfs.rpc.codec;

/**
 * Decoder Interface
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface Decoder {

	/**
	 * decode byte[] to Object
	 * 
	 * @param className className
	 * @param bytes     bytes
	 * @return obj
	 * @throws CodecException CodecException
	 */
	Object decode(String className, byte[] bytes) throws CodecException;

}
