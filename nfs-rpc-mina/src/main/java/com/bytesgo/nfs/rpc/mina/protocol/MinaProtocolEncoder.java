package com.bytesgo.nfs.rpc.mina.protocol;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.bytesgo.nfs.rpc.core.protocol.ProtocolUtils;

/**
 * Encode message
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaProtocolEncoder extends ProtocolEncoderAdapter {

  public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
    MinaByteBufferWrapper wrapper = new MinaByteBufferWrapper();
    ProtocolUtils.encode(message, wrapper);
    wrapper.getByteBuffer().flip();
    out.write(wrapper.getByteBuffer());
  }

}
