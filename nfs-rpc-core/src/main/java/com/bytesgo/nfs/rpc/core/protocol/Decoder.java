/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
package com.bytesgo.nfs.rpc.core.protocol;

/**
 * Decoder Interface
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface Decoder {

  /**
   * decode byte[] to Object
   */
   Object decode(String className, byte[] bytes) throws Exception;

}
