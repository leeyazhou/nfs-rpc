package com.xyz.rpc.grizzly.benchmark;

import java.util.List;

import org.glassfish.grizzly.threadpool.GrizzlyExecutorService;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;

import com.xyz.rpc.benchmark.AbstractSimpleProcessorBenchmarkClient;
import com.xyz.rpc.benchmark.ClientRunnable;
import com.xyz.rpc.client.ClientFactory;
import com.xyz.rpc.grizzly.client.GrizzlyClientFactory;

/**
 * Grizzly Simple Benchmark Client
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class GrizzlySimpleBenchmarkClient extends AbstractSimpleProcessorBenchmarkClient {

  public static void main(String[] args) throws Exception {
    new GrizzlySimpleBenchmarkClient().run(args);
  }

  public ClientFactory getClientFactory() {
    return GrizzlyClientFactory.getInstance();
  }

  protected void startRunnables(List<ClientRunnable> runnables) {
    ThreadPoolConfig tpc = ThreadPoolConfig.defaultConfig().copy().setPoolName("benchmarkclient").setMaxPoolSize(runnables.size())
        .setCorePoolSize(runnables.size());

    GrizzlyExecutorService executorService = GrizzlyExecutorService.createInstance(tpc);

    for (int i = 0; i < runnables.size(); i++) {
      ClientRunnable runnable = runnables.get(i);
      executorService.execute(runnable);
    }
  }
}
