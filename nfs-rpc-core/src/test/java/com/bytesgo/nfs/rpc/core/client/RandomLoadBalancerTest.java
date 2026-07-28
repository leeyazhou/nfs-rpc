package com.bytesgo.nfs.rpc.core.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link RandomLoadBalancer}.
 *
 * @author leeyazhou
 */
class RandomLoadBalancerTest {

	@Test
	void select_singleServer_returnsThatServer() {
		LoadBalancer lb = new RandomLoadBalancer();
		InetSocketAddress server = new InetSocketAddress("127.0.0.1", 8080);

		InetSocketAddress selected = lb.select(Collections.singletonList(server));

		assertThat(selected).isSameAs(server);
	}

	@Test
	void select_multipleServers_returnsOneOfThem() {
		LoadBalancer lb = new RandomLoadBalancer();
		InetSocketAddress s1 = new InetSocketAddress("10.0.0.1", 8080);
		InetSocketAddress s2 = new InetSocketAddress("10.0.0.2", 8080);
		InetSocketAddress s3 = new InetSocketAddress("10.0.0.3", 8080);
		List<InetSocketAddress> servers = Arrays.asList(s1, s2, s3);

		for (int i = 0; i < 50; i++) {
			InetSocketAddress selected = lb.select(servers);
			assertThat(selected).isIn(servers);
		}
	}

	@Test
	void select_emptyList_throwsException() {
		LoadBalancer lb = new RandomLoadBalancer();

		assertThatThrownBy(() -> lb.select(Collections.emptyList()))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void select_nullList_throwsException() {
		LoadBalancer lb = new RandomLoadBalancer();

		assertThatThrownBy(() -> lb.select(null))
				.isInstanceOf(NullPointerException.class);
	}
}
