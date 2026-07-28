package com.bytesgo.nfs.rpc.core.client;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Strategy for selecting a server from a list of candidate servers.
 *
 * @author leeyazhou
 */
public interface LoadBalancer {

	/**
	 * Select a server from the candidate list.
	 *
	 * @param servers list of candidate servers, must not be empty
	 * @return the selected server, never null
	 */
	InetSocketAddress select(List<InetSocketAddress> servers);

}
