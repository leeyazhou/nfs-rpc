package com.xyz.rpc.protocol.codecs;

import com.esotericsoftware.kryo.io.Input;
import com.xyz.rpc.benchmark.KryoUtils;
import com.xyz.rpc.protocol.Decoder;

/**
 * Kryo Decoder
 * 
 * @author <a href="mailto:jlusdy@gmail.com">jlusdy</a>
 */
public class KryoDecoder implements Decoder {
  /**
   * @param className
   * @param bytes
   * @return
   * @throws Exception
   */
  @Override
  public Object decode(String className, byte[] bytes) throws Exception {
    Input input = new Input(bytes);
    try {
      return KryoUtils.getKryo().readClassAndObject(input);
    } finally {
      input.close();
    }
  }
}
