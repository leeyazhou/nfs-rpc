package com.xyz.rpc.netty.benchmark;

import com.xyz.rpc.benchmark.AbstractSimpleProcessorBenchmarkClient;
import com.xyz.rpc.client.ClientFactory;
import com.xyz.rpc.netty.client.NettyClientFactory;

/**
 * Netty Direct Call RPC Benchmark Client
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettySimpleBenchmarkClient extends AbstractSimpleProcessorBenchmarkClient {

  public static void main(String[] args) throws Exception {
    new NettySimpleBenchmarkClient().run(args);
  }

  public ClientFactory getClientFactory() {
    return NettyClientFactory.getInstance();
  }

}
