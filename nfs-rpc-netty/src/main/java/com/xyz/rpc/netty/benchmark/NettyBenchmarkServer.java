package com.xyz.rpc.netty.benchmark;

import com.xyz.rpc.benchmark.AbstractBenchmarkServer;
import com.xyz.rpc.netty.server.NettyServer;
import com.xyz.rpc.server.Server;

/**
 * Netty RPC Benchmark Server
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettyBenchmarkServer extends AbstractBenchmarkServer {

  public static void main(String[] args) throws Exception {
    new NettyBenchmarkServer().run(args);
  }

  public Server getServer() {
    return new NettyServer();
  }

}
