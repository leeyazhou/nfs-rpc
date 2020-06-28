package com.bytesgo.nfs.rpc.netty.protocol;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.bytesgo.nfs.rpc.core.protocol.ProtocolUtils;

/**
 * Encode Message
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettyProtocolEncoder extends OneToOneEncoder {

  protected Object encode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {
    NettyByteBufferWrapper byteBufferWrapper = new NettyByteBufferWrapper();
    ProtocolUtils.encode(message, byteBufferWrapper);
    return byteBufferWrapper.getBuffer();
  }

}
