package com.bytesgo.nfs.rpc.benchmark.mina2;

import com.bytesgo.nfs.rpc.benchmark.AbstractBenchmarkServer;
import com.bytesgo.nfs.rpc.core.server.Server;
import com.bytesgo.nfs.rpc.core.server.ServerConfig;
import com.bytesgo.nfs.rpc.mina2.server.MinaServer;

/**
 * Mina RPC Benchmark Server
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaBenchmarkServer extends AbstractBenchmarkServer {

  public static void main(String[] args) throws Exception {
    new MinaBenchmarkServer().run(args);
  }

  public Server getServer(ServerConfig serverConfig) {
    return new MinaServer(serverConfig);
  }

}
