package com.bytesgo.nfs.rpc.core.message;

/**
 * RequestWrapper
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class RequestMessage extends Message {
	private static final long serialVersionUID = 1L;
	private byte[] targetInstanceName;

	private byte[] methodName;

	private byte[][] argTypes;

	private Object[] args = null;

	private Object message = null;

	private int timeout = 0;

	private int messageLen;

	public RequestMessage(Object message, int timeout, int codecType, int protocolType) {
		this(message, timeout, incId.incrementAndGet(), codecType, protocolType);
	}

	public RequestMessage(Object message, int timeout, int id, int codecType, int protocolType) {
		this.message = message;
		setId(id);
		this.timeout = timeout;
		setCodecType(codecType);
		setProtocolType(protocolType);
	}

	public RequestMessage(byte[] targetInstanceName, byte[] methodName, byte[][] argTypes, Object[] args, int timeout,
			int codecType, int protocolType) {
		this(targetInstanceName, methodName, argTypes, args, timeout, incId.incrementAndGet(), codecType, protocolType);
	}

	public RequestMessage(byte[] targetInstanceName, byte[] methodName, byte[][] argTypes, Object[] args, int timeout,
			int id, int codecType, int protocolType) {
		this.args = args;
		setId(id);
		this.timeout = timeout;
		this.targetInstanceName = targetInstanceName;
		this.methodName = methodName;
		this.argTypes = argTypes;
		setCodecType(codecType);
		setProtocolType(protocolType);
	}

	public int getMessageLen() {
		return messageLen;
	}

	public RequestMessage setMessageLen(int messageLen) {
		this.messageLen = messageLen;
		return this;
	}

	public RequestMessage setArgTypes(byte[][] argTypes) {
		this.argTypes = argTypes;
		return this;
	}

	public Object getMessage() {
		return message;
	}

	public byte[] getTargetInstanceName() {
		return targetInstanceName;
	}

	public byte[] getMethodName() {
		return methodName;
	}

	public int getTimeout() {
		return timeout;
	}

	public Object[] getArgs() {
		return args;
	}

	public byte[][] getArgTypes() {
		return argTypes;
	}

}
