package com.bytesgo.nfs.rpc.benchmark.mina;

import com.bytesgo.nfs.rpc.benchmark.AbstractSimpleProcessorBenchmarkClient;
import com.bytesgo.nfs.rpc.core.client.ClientFactory;
import com.bytesgo.nfs.rpc.mina2.client.MinaClientFactory;

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
