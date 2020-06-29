/**
 * 
 */
package com.bytesgo.nfs.rpc.core.codec;

/**
 * @author leeyazhou
 *
 */
public interface Codec {

	byte[] encode(Object object) throws CodecException;

	Object decode(String className, byte[] bytes) throws CodecException;

}
