package com.bytesgo.nfs.rpc.core.client;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Random load balancer - selects a server randomly from the candidate list.
 * <p>
 * Uses {@link ThreadLocalRandom} for thread-safe, contention-free random selection.
 * </p>
 *
 * @author leeyazhou
 */
public class RandomLoadBalancer implements LoadBalancer {

	@Override
	public InetSocketAddress select(List<InetSocketAddress> servers) {
		if (servers.size() == 1) {
			return servers.get(0);
		}
		return servers.get(ThreadLocalRandom.current().nextInt(servers.size()));
	}

}
