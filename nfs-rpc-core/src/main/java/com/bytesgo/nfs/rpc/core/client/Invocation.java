/**
 * 
 */
package com.bytesgo.nfs.rpc.core.client;

import java.io.Serializable;

/**
 * @author leeyazhou
 *
 */
public class Invocation implements Serializable {
	private static final long serialVersionUID = 1L;
	private String processorName;
	private String methodName;
	private String[] argTypes;
	private Object[] args;

	public String getProcessorName() {
		return processorName;
	}

	public Invocation setProcessorName(String processorName) {
		this.processorName = processorName;
		return this;
	}

	public String getMethodName() {
		return methodName;
	}

	public Invocation setMethodName(String methodName) {
		this.methodName = methodName;
		return this;
	}

	public String[] getArgTypes() {
		return argTypes;
	}

	public Invocation setArgTypes(String[] argTypes) {
		this.argTypes = argTypes;
		return this;
	}

	public Object[] getArgs() {
		return args;
	}

	public Invocation setArgs(Object[] args) {
		this.args = args;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
