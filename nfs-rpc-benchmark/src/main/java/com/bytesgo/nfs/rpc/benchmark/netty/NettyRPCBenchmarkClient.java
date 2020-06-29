package com.bytesgo.nfs.rpc.benchmark.netty;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.bytesgo.nfs.rpc.benchmark.AbstractRPCBenchmarkClient;
import com.bytesgo.nfs.rpc.benchmark.service.BenchmarkService;
import com.bytesgo.nfs.rpc.netty.client.NettyClientInvocationHandler;

/**
 * Netty RPC Benchmark Client
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettyRPCBenchmarkClient extends AbstractRPCBenchmarkClient {

  public static void main(String[] args) throws Exception {
    new NettyRPCBenchmarkClient().run(args);
  }

  public BenchmarkService getProxyInstance(List<InetSocketAddress> servers, int clientNums, int connectTimeout,
      String targetInstanceName, Map<String, Integer> methodTimeouts, int codectype, Integer protocolType) {
    return (BenchmarkService) Proxy.newProxyInstance(NettyRPCBenchmarkClient.class.getClassLoader(),
        new Class<?>[] { BenchmarkService.class },
        new NettyClientInvocationHandler(servers, clientNums, connectTimeout, targetInstanceName, methodTimeouts, codectype, protocolType));
  }

}
