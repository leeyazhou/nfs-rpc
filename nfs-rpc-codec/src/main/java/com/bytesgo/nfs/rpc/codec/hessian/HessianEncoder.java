package com.bytesgo.nfs.rpc.codec.hessian;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.ByteArrayOutputStream;

import com.bytesgo.nfs.rpc.codec.CodecException;
import com.bytesgo.nfs.rpc.codec.Encoder;
import com.caucho.hessian.io.Hessian2Output;

/**
 * Hessian Encoder,use Hessian2
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class HessianEncoder implements Encoder {

	public byte[] encode(Object object) throws CodecException {
		try {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			Hessian2Output output = new Hessian2Output(byteArray);
			output.writeObject(object);
			output.close();
			return byteArray.toByteArray();
		} catch (Exception e) {
			throw new CodecException(e);
		}
	}

}
