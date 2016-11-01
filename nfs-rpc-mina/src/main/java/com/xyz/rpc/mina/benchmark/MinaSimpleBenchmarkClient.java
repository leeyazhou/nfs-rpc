package com.xyz.rpc.mina.benchmark;

import com.xyz.rpc.benchmark.AbstractSimpleProcessorBenchmarkClient;
import com.xyz.rpc.client.ClientFactory;
import com.xyz.rpc.mina.client.MinaClientFactory;

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
