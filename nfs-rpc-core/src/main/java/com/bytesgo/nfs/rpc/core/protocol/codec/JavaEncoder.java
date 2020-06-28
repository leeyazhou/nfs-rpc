package com.bytesgo.nfs.rpc.core.protocol.codec;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import com.bytesgo.nfs.rpc.core.protocol.Encoder;

/**
 * Java Encoder
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class JavaEncoder implements Encoder {

  public byte[] encode(Object object) throws Exception {
    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
    ObjectOutputStream output = new ObjectOutputStream(byteArray);
    output.writeObject(object);
    output.flush();
    output.close();
    return byteArray.toByteArray();
  }

}
