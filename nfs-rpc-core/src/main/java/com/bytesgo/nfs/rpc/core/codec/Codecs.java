package com.bytesgo.nfs.rpc.core.codec;

import com.bytesgo.nfs.rpc.core.codec.hessian.HessianDecoder;
import com.bytesgo.nfs.rpc.core.codec.hessian.HessianEncoder;
import com.bytesgo.nfs.rpc.core.codec.java.JavaDecoder;
import com.bytesgo.nfs.rpc.core.codec.java.JavaEncoder;
import com.bytesgo.nfs.rpc.core.codec.kryo.KryoDecoder;
import com.bytesgo.nfs.rpc.core.codec.kryo.KryoEncoder;
import com.bytesgo.nfs.rpc.core.codec.protobuf.ProtobufDecoder;
import com.bytesgo.nfs.rpc.core.codec.protobuf.ProtobufEncoder;

/**
 * Encoder & Decoder Register
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class Codecs {

  public static final int JAVA_CODEC = 1;

  public static final int HESSIAN_CODEC = 2;

  public static final int PB_CODEC = 3;

  public static final int KRYO_CODEC = 4;

  private static Encoder[] encoders = new Encoder[5];

  private static Decoder[] decoders = new Decoder[5];

  static {
    addEncoder(JAVA_CODEC, new JavaEncoder());
    addDecoder(JAVA_CODEC, new JavaDecoder());

    addEncoder(HESSIAN_CODEC, new HessianEncoder());
    addDecoder(HESSIAN_CODEC, new HessianDecoder());

    addEncoder(PB_CODEC, new ProtobufEncoder());
    addDecoder(PB_CODEC, new ProtobufDecoder());

    addEncoder(KRYO_CODEC, new KryoEncoder());
    addDecoder(KRYO_CODEC, new KryoDecoder());
  }

  public static void addEncoder(int encoderKey, Encoder encoder) {
    if (encoderKey > encoders.length) {
      Encoder[] newEncoders = new Encoder[encoderKey + 1];
      System.arraycopy(encoders, 0, newEncoders, 0, encoders.length);
      encoders = newEncoders;
    }
    encoders[encoderKey] = encoder;
  }

  public static void addDecoder(int decoderKey, Decoder decoder) {
    if (decoderKey > decoders.length) {
      Decoder[] newDecoders = new Decoder[decoderKey + 1];
      System.arraycopy(decoders, 0, newDecoders, 0, decoders.length);
      decoders = newDecoders;
    }
    decoders[decoderKey] = decoder;
  }

  public static Encoder getEncoder(int encoderKey) {
    return encoders[encoderKey];
  }

  public static Decoder getDecoder(int decoderKey) {
    return decoders[decoderKey];
  }

}
