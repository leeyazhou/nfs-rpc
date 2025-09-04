/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
package com.bytesgo.nfs.rpc.codec;

/**
 * Encoder Interface
 * 
 * @author leeyazhou
 */
public interface Encoder {

	/**
	 * Encode Object to byte[]
	 * 
	 * @param object data
	 * @return bytes
	 * @throws CodecException CodecException
	 */
	byte[] encode(Object object) throws CodecException;

}
