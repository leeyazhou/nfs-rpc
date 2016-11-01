package com.xyz.rpc.protocol.codecs;

import com.esotericsoftware.kryo.io.Output;
import com.xyz.rpc.benchmark.KryoUtils;
import com.xyz.rpc.protocol.Encoder;

/**
 * Kryo Encoder
 * 
 * @author <a href="mailto:jlusdy@gmail.com">jlusdy</a>
 */
public class KryoEncoder implements Encoder {
  /**
   * @param object
   * @return
   * @throws Exception
   */
  @Override
  public byte[] encode(Object object) throws Exception {
    Output output = new Output(256);
    KryoUtils.getKryo().writeClassAndObject(output, object);
    try {
      return output.toBytes();
    } finally {
      output.close();
    }
  }

}
