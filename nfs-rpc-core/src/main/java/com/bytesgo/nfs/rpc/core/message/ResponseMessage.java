package com.bytesgo.nfs.rpc.core.message;

/**
 * ResponseWrapper
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class ResponseMessage extends Message {
	private static final long serialVersionUID = 1L;

	private Object response = null;

	private boolean error = false;

	private Throwable exception = null;

	private int messageLen;

	private byte[] responseClassName;

	public ResponseMessage(int id, int codecType, int protocolType) {
		setId(id);
		setCodecType(codecType);
		setProtocolType(protocolType);
	}

	public int getMessageLen() {
		return messageLen;
	}

	public ResponseMessage setMessageLen(int messageLen) {
		this.messageLen = messageLen;
		return this;
	}

	public Object getResponse() {
		return response;
	}

	public ResponseMessage setResponse(Object response) {
		this.response = response;
		return this;
	}

	public boolean isError() {
		return error;
	}

	public Throwable getException() {
		return exception;
	}

	public ResponseMessage setException(Throwable exception) {
		this.exception = exception;
		error = true;
		return this;
	}

	public byte[] getResponseClassName() {
		return responseClassName;
	}

	public ResponseMessage setResponseClassName(byte[] responseClassName) {
		this.responseClassName = responseClassName;
		return this;
	}

}
