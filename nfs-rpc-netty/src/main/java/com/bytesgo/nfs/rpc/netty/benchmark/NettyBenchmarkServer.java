package com.bytesgo.nfs.rpc.netty.benchmark;

import com.bytesgo.nfs.rpc.common.benchmark.AbstractBenchmarkServer;
import com.bytesgo.nfs.rpc.common.server.Server;
import com.bytesgo.nfs.rpc.netty.server.NettyServer;

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
