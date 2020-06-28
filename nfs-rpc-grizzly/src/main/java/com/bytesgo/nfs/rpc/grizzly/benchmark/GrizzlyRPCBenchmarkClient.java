package com.bytesgo.nfs.rpc.grizzly.benchmark;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.bytesgo.nfs.rpc.core.benchmark.AbstractRPCBenchmarkClient;
import com.bytesgo.nfs.rpc.core.benchmark.BenchmarkTestService;
import com.bytesgo.nfs.rpc.grizzly.client.GrizzlyClientInvocationHandler;

/**
 * Grizzly RPC Benchmark Client
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class GrizzlyRPCBenchmarkClient extends AbstractRPCBenchmarkClient {

  public static void main(String[] args) throws Exception {
    new GrizzlyRPCBenchmarkClient().run(args);
  }

  public BenchmarkTestService getProxyInstance(List<InetSocketAddress> servers, int clientNums, int connectTimeout,
      String targetInstanceName, Map<String, Integer> methodTimeouts, int codectype, Integer protocolType) {
    return (BenchmarkTestService) Proxy.newProxyInstance(GrizzlyRPCBenchmarkClient.class.getClassLoader(),
        new Class<?>[] { BenchmarkTestService.class }, new GrizzlyClientInvocationHandler(servers, clientNums, connectTimeout,
            targetInstanceName, methodTimeouts, codectype, protocolType));
  }

}
