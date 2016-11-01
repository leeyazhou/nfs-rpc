package com.xyz.rpc.grizzly.client;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.xyz.rpc.client.AbstractClientInvocationHandler;
import com.xyz.rpc.client.ClientFactory;

/**
 * Grizzly Client Invocation Handler for RPC
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class GrizzlyClientInvocationHandler extends AbstractClientInvocationHandler {

  public GrizzlyClientInvocationHandler(List<InetSocketAddress> servers, int clientNums, int connectTimeout, String targetInstanceName,
      Map<String, Integer> methodTimeouts, int codecType, int protocolType) {
    super(servers, clientNums, connectTimeout, targetInstanceName, methodTimeouts, codecType, protocolType);
  }

  public ClientFactory getClientFactory() {
    return GrizzlyClientFactory.getInstance();
  }

}
