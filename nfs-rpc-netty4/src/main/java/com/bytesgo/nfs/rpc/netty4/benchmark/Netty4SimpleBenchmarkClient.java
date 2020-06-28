package com.bytesgo.nfs.rpc.netty4.benchmark;

import com.bytesgo.nfs.rpc.core.Codecs;
import com.bytesgo.nfs.rpc.core.benchmark.AbstractSimpleProcessorBenchmarkClient;
import com.bytesgo.nfs.rpc.core.client.ClientFactory;
import com.bytesgo.nfs.rpc.netty4.client.Netty4ClientFactory;

/**
 * Netty4 Direct Call RPC Benchmark Client
 * 
 * @author <a href="mailto:coderplay@gmail.com">Min Zhou</a>
 */
public class Netty4SimpleBenchmarkClient extends AbstractSimpleProcessorBenchmarkClient {

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
    new Netty4SimpleBenchmarkClient().run(args);
  }

  public ClientFactory getClientFactory() {
    return Netty4ClientFactory.getInstance();
  }

}
