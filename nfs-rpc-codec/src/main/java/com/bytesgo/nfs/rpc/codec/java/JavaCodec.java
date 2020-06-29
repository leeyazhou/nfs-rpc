/**
 * 
 */
package com.bytesgo.nfs.rpc.codec.java;

import com.bytesgo.nfs.rpc.codec.Codec;
import com.bytesgo.nfs.rpc.codec.CodecException;
import com.bytesgo.nfs.rpc.codec.Decoder;
import com.bytesgo.nfs.rpc.codec.Encoder;

/**
 * @author leeyazhou
 *
 */
public class JavaCodec implements Codec {

	private Encoder encoder = new JavaEncoder();
	private Decoder decoder = new JavaDecoder();

	@Override
	public byte[] encode(Object object) throws CodecException {
		return encoder.encode(object);
	}

	@Override
	public Object decode(String className, byte[] bytes) throws CodecException {
		return decoder.decode(className, bytes);
	}

}
