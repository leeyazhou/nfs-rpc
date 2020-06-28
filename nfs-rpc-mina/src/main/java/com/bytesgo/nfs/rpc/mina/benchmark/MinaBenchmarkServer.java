package com.bytesgo.nfs.rpc.mina.benchmark;

import com.bytesgo.nfs.rpc.common.benchmark.AbstractBenchmarkServer;
import com.bytesgo.nfs.rpc.common.server.Server;
import com.bytesgo.nfs.rpc.mina.server.MinaServer;

/**
 * Mina RPC Benchmark Server
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaBenchmarkServer extends AbstractBenchmarkServer {

  public static void main(String[] args) throws Exception {
    new MinaBenchmarkServer().run(args);
  }

  public Server getServer() {
    return new MinaServer();
  }

}
