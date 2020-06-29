/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
package com.bytesgo.nfs.rpc.core.protocol;

import com.bytesgo.nfs.rpc.core.message.Message;

/**
 * Protocol Header VERSION(1B): Protocol Version TYPE(1B): Protocol Type,so u
 * can custom your protocol CUSTOM PROTOCOL (such as RPCProtocol)
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class ProtocolUtils {

	public static final int HEADER_LEN = 2;

	public static final byte CURRENT_VERSION = (byte) 1;

	public static ByteBufferWrapper encode(Object message, ByteBufferWrapper bytebufferWrapper) throws Exception {
		int protocolType = ((Message) message).getProtocolType();
		return ProtocolFactory.getProtocol(protocolType).encode(message, bytebufferWrapper);
	}

	public static Object decode(ByteBufferWrapper wrapper, Object errorObject) throws ProtocolException {
		final int originPos = wrapper.readerIndex();
		if (wrapper.readableBytes() < HEADER_LEN) {
			wrapper.setReaderIndex(originPos);
			return errorObject;
		}
		int version = wrapper.readByte();
		if (version == CURRENT_VERSION) {
			int type = wrapper.readByte();
			Protocol protocol = ProtocolFactory.getProtocol(type);
			if (protocol == null) {
				throw new ProtocolException("Unsupport protocol type: " + type);
			}
			return ProtocolFactory.getProtocol(type).decode(wrapper, errorObject, new int[] { originPos });
		} else {
			throw new ProtocolException("Unsupport protocol version: " + version);
		}
	}

}
