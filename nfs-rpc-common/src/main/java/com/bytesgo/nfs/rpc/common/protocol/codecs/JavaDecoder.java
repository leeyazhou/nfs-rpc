package com.bytesgo.nfs.rpc.common.protocol.codecs;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import com.bytesgo.nfs.rpc.common.protocol.Decoder;

/**
 * Java Decoder
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class JavaDecoder implements Decoder {

  public Object decode(String className, byte[] bytes) throws Exception {
    ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(bytes));
    Object resultObject = objectIn.readObject();
    objectIn.close();
    return resultObject;
  }

}
