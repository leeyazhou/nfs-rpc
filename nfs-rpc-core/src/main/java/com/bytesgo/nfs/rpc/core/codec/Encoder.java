/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
package com.bytesgo.nfs.rpc.core.codec;

/**
 * Encoder Interface
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface Encoder {

  /**
   * Encode Object to byte[]
   */
   byte[] encode(Object object) throws CodecException;

}
