package com.bytesgo.nfs.rpc.codec;

import com.bytesgo.nfs.rpc.codec.hessian.HessianCodec;
import com.bytesgo.nfs.rpc.codec.java.JavaCodec;
import com.bytesgo.nfs.rpc.codec.kryo.KryoCodec;
import com.bytesgo.nfs.rpc.codec.protobuf.ProtobufCodec;

/**
 * Encoder and Decoder Register
 * 
 * @author leeyazhou
 */
public class Codecs {

	public static final int JAVA_CODEC = 1;

	public static final int HESSIAN_CODEC = 2;

	public static final int PB_CODEC = 3;

	public static final int KRYO_CODEC = 4;

	private static Codec[] codecs = new Codec[5];

	static {
		addCodec(JAVA_CODEC, new JavaCodec());

		addCodec(HESSIAN_CODEC, new HessianCodec());

		addCodec(PB_CODEC, new ProtobufCodec());

		addCodec(KRYO_CODEC, new KryoCodec());
	}

	public static void addCodec(int encoderKey, Codec codec) {
		if (encoderKey > codecs.length) {
			Codec[] newEncoders = new Codec[encoderKey + 1];
			System.arraycopy(codecs, 0, newEncoders, 0, codecs.length);
			codecs = newEncoders;
		}
		codecs[encoderKey] = codec;
	}

	public static Codec getCodec(int decoderKey) {
		return codecs[decoderKey];
	}

}
