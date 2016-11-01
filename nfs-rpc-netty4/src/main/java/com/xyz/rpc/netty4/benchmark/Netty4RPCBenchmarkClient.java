package com.xyz.rpc.netty4.benchmark;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.xyz.rpc.Codecs;
import com.xyz.rpc.benchmark.AbstractRPCBenchmarkClient;
import com.xyz.rpc.benchmark.BenchmarkTestService;
import com.xyz.rpc.netty4.client.Netty4ClientInvocationHandler;

/**
 * Netty4 RPC Benchmark Client
 * 
 * @author <a href="mailto:coderplay@gmail.com">Min Zhou</a>
 */
public class Netty4RPCBenchmarkClient extends AbstractRPCBenchmarkClient {

  public static void main(String[] args) throws Exception {
    if (args.length != 7) {
      args = new String[7];
      args[0] = "127.0.0.1";
      args[1] = "12200";// port
      args[2] = "100"; // concurrents
      args[3] = "2000"; // timeout
      args[4] = Codecs.PB_CODEC + ""; // codecType
      args[5] = "100"; // requestsize
      args[6] = "60"; // runtime(seconds)
    }
    new Netty4RPCBenchmarkClient().run(args);
  }

  @Override
  public BenchmarkTestService getProxyInstance(List<InetSocketAddress> servers, int clientNums, int connectTimeout,
      String targetInstanceName, Map<String, Integer> methodTimeouts, int codectype, Integer protocolType) {
    return (BenchmarkTestService) Proxy.newProxyInstance(Netty4RPCBenchmarkClient.class.getClassLoader(),
        new Class<?>[] { BenchmarkTestService.class }, new Netty4ClientInvocationHandler(servers, clientNums, connectTimeout,
            targetInstanceName, methodTimeouts, codectype, protocolType));
  }

}
