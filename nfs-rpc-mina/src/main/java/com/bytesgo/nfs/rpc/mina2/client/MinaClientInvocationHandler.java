package com.bytesgo.nfs.rpc.mina2.client;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.bytesgo.nfs.rpc.core.client.AbstractClientInvocationHandler;
import com.bytesgo.nfs.rpc.core.client.ClientFactory;

/**
 * Mina Client Invocation Handler for proxy
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaClientInvocationHandler extends AbstractClientInvocationHandler {

  public MinaClientInvocationHandler(List<InetSocketAddress> servers, int clientNums, int connectTimeout, String targetInstanceName,
      Map<String, Integer> methodTimeouts, int codecType, int protocolType) {
    super(servers, clientNums, connectTimeout, targetInstanceName, methodTimeouts, codecType, protocolType);
  }

  public ClientFactory getClientFactory() {
    return MinaClientFactory.getInstance();
  }

}
