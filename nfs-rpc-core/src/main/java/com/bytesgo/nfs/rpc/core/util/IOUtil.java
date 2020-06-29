/**
 * 
 */
package com.bytesgo.nfs.rpc.core.util;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leeyazhou
 *
 */
public class IOUtil {
	private static final Logger logger = LoggerFactory.getLogger(IOUtil.class);

	public static void close(Closeable close) {
		if (close != null) {
			try {
				close.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}
}
