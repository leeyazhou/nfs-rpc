/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
package com.bytesgo.nfs.rpc.core.protocol;

/**
 * ByteBufferWrapper interface,help for intergrate different network framework
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface ByteBufferWrapper {

	ByteBufferWrapper get(int capacity);

	void writeByte(int index, byte data);

	void writeByte(byte data);

	byte readByte();

	void writeInt(int data);

	void writeBytes(byte[] data);

	int readableBytes();

	int readInt();

	void readBytes(byte[] dst);

	int readerIndex();

	void setReaderIndex(int readerIndex);

}
