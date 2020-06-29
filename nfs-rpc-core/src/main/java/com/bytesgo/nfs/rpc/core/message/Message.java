/**
 * 
 */
package com.bytesgo.nfs.rpc.core.message;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import com.bytesgo.nfs.rpc.core.codec.Codecs;

/**
 * @author leeyazhou
 *
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	protected static final AtomicInteger incId = new AtomicInteger(0);
	private int id = 0;
	private int protocolType;
	private int codecType = Codecs.HESSIAN_CODEC;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}

	public void setCodecType(int codecType) {
		this.codecType = codecType;
	}

	public int getProtocolType() {
		return protocolType;
	}

	public int getCodecType() {
		return codecType;
	}

	public int id() {
		return id;
	}
}
