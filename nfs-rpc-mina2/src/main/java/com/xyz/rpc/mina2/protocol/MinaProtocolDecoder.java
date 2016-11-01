package com.xyz.rpc.mina2.protocol;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.xyz.rpc.protocol.ProtocolUtils;

/**
 * decode receive message
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaProtocolDecoder extends CumulativeProtocolDecoder {

  protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
    MinaByteBufferWrapper wrapper = new MinaByteBufferWrapper(in);
    Object returnObject = ProtocolUtils.decode(wrapper, false);
    if (returnObject instanceof Boolean) {
      return false;
    }
    out.write(returnObject);
    return true;
  }

}
