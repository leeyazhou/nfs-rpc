package com.bytesgo.nfs.rpc.mina.benchmark;

import com.bytesgo.nfs.rpc.common.benchmark.AbstractSimpleProcessorBenchmarkClient;
import com.bytesgo.nfs.rpc.common.client.ClientFactory;
import com.bytesgo.nfs.rpc.mina.client.MinaClientFactory;

/**
 * Mina Simple Benchmark Client
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaSimpleBenchmarkClient extends AbstractSimpleProcessorBenchmarkClient {

  public static void main(String[] args) throws Exception {
    new MinaSimpleBenchmarkClient().run(args);
  }

  public ClientFactory getClientFactory() {
    return MinaClientFactory.getInstance();
  }

}
