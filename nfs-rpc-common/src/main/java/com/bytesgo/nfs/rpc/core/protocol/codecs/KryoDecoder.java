package com.bytesgo.nfs.rpc.core.protocol.codecs;

import com.bytesgo.nfs.rpc.core.benchmark.KryoUtils;
import com.bytesgo.nfs.rpc.core.protocol.Decoder;
import com.esotericsoftware.kryo.io.Input;

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