package com.xyz.rpc.mina.benchmark;

import com.xyz.rpc.benchmark.AbstractBenchmarkServer;
import com.xyz.rpc.mina.server.MinaServer;
import com.xyz.rpc.server.Server;

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
