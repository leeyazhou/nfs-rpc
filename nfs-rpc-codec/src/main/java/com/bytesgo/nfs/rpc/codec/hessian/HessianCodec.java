/**
 * 
 */
package com.bytesgo.nfs.rpc.codec.hessian;

import com.bytesgo.nfs.rpc.codec.Codec;
import com.bytesgo.nfs.rpc.codec.CodecException;
import com.bytesgo.nfs.rpc.codec.Decoder;
import com.bytesgo.nfs.rpc.codec.Encoder;

/**
 * @author leeyazhou
 *
 */
public class HessianCodec implements Codec {

	private Encoder encoder = new HessianEncoder();
	private Decoder decoder = new HessianDecoder();

	@Override
	public byte[] encode(Object object) throws CodecException {
		return encoder.encode(object);
	}

	@Override
	public Object decode(String className, byte[] bytes) throws CodecException {
		return decoder.decode(className, bytes);
	}

}
