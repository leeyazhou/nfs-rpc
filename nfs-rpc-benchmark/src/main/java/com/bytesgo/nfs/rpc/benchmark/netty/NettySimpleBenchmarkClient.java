package com.bytesgo.nfs.rpc.benchmark.netty;

import com.bytesgo.nfs.rpc.benchmark.AbstractSimpleProcessorBenchmarkClient;
import com.bytesgo.nfs.rpc.core.client.ClientFactory;
import com.bytesgo.nfs.rpc.netty.client.NettyClientFactory;

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
