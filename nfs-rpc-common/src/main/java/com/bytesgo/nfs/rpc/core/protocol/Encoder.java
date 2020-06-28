/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
package com.bytesgo.nfs.rpc.core.protocol;

/**
 * Encoder Interface
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface Encoder {

  /**
   * Encode Object to byte[]
   */
  public byte[] encode(Object object) throws Exception;

}
