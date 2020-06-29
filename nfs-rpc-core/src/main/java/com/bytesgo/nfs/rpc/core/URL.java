/**
 * 
 */
package com.bytesgo.nfs.rpc.core;

/**
 * @author leeyazhou
 *
 */
public class URL {

	private String host;
	private String protocol;
	private int port;

	public String getHost() {
		return host;
	}

	public URL setHost(String host) {
		this.host = host;
		return this;
	}

	public String getProtocol() {
		return protocol;
	}

	public URL setProtocol(String protocol) {
		this.protocol = protocol;
		return this;
	}

	public int getPort() {
		return port;
	}

	public URL setPort(int port) {
		this.port = port;
		return this;
	}

}
