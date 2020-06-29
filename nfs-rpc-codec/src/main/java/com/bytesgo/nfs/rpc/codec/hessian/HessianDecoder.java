package com.bytesgo.nfs.rpc.codec.hessian;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.codec.CodecException;
import com.bytesgo.nfs.rpc.codec.Decoder;
import com.caucho.hessian.io.Hessian2Input;

/**
 * Hessian Decoder,use Hessian2
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class HessianDecoder implements Decoder {
	private static final Logger logger = LoggerFactory.getLogger(HessianDecoder.class);

	public Object decode(String className, byte[] bytes) throws CodecException {
		Hessian2Input input = null;
		try {
			input = new Hessian2Input(new ByteArrayInputStream(bytes));
			// avoid child object to parent object problem
			Object resultObject = input.readObject();
			input.close();
			return resultObject;
		} catch (Exception e) {
			throw new CodecException(e);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}

	}

}
