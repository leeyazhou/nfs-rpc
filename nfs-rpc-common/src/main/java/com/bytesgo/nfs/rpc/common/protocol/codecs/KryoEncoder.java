package com.bytesgo.nfs.rpc.common.protocol.codecs;

import com.bytesgo.nfs.rpc.common.benchmark.KryoUtils;
import com.bytesgo.nfs.rpc.common.protocol.Encoder;
import com.esotericsoftware.kryo.io.Output;

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
