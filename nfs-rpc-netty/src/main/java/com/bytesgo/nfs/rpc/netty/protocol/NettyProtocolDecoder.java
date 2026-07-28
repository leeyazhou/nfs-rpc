package com.bytesgo.nfs.rpc.netty.protocol;

import java.util.List;

import com.bytesgo.nfs.rpc.core.protocol.ProtocolUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
/**
 * decode byte[] change to pipeline receive requests or responses,let's IO
 * thread do less thing
 * 
 * @author <a href="mailto:coderplay@gmail.com">Min Zhou</a>
 */
public class NettyProtocolDecoder extends ByteToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    NettyByteBufferWrapper wrapper = new NettyByteBufferWrapper(in);
    Object msg = ProtocolUtils.decode(wrapper, null);
    if (msg != null)
      out.add(msg);
  }

}
