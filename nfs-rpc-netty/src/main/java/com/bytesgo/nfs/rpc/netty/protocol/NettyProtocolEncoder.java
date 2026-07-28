package com.bytesgo.nfs.rpc.netty.protocol;

import com.bytesgo.nfs.rpc.core.protocol.ProtocolUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
/**
 * Encode Message
 * 
 * @author <a href="mailto:coderplay@gmail.com">Min Zhou</a>
 */
public class NettyProtocolEncoder extends MessageToByteEncoder<Object> {

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
    NettyByteBufferWrapper byteBufferWrapper = new NettyByteBufferWrapper(out);
    ProtocolUtils.encode(msg, byteBufferWrapper);
  }

}
