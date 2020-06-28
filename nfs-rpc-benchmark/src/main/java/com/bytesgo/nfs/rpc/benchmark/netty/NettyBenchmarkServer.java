package com.bytesgo.nfs.rpc.benchmark.netty;

import com.bytesgo.nfs.rpc.benchmark.AbstractBenchmarkServer;
import com.bytesgo.nfs.rpc.core.server.Server;
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
