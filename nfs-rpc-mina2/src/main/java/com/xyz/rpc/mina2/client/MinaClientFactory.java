package com.xyz.rpc.mina2.client;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyz.rpc.client.AbstractClientFactory;
import com.xyz.rpc.client.Client;
import com.xyz.rpc.mina2.protocol.MinaProtocolCodecFilter;

/**
 * Mina Client Factory
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaClientFactory extends AbstractClientFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(MinaClientFactory.class);

  private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();

  private static final int processorCount = Runtime.getRuntime().availableProcessors() + 1;

  private static final AbstractClientFactory _self = new MinaClientFactory();

  private SocketConnector ioConnector;

  private MinaClientFactory() {
    // only one ioConnector,avoid create too many io processor thread
    ioConnector = new NioSocketConnector(processorCount);
    // ioConnector.getFilterChain().addLast("executor", new
    // ExecutorFilter(Executors.newCachedThreadPool()));
    ioConnector.getSessionConfig().setTcpNoDelay(Boolean.parseBoolean(System.getProperty("nfs.rpc.tcp.nodelay", "true")));
    ioConnector.getSessionConfig().setReuseAddress(true);
    ioConnector.getFilterChain().addLast("objectserialize", new MinaProtocolCodecFilter());
  }

  public static AbstractClientFactory getInstance() {
    return _self;
  }

  protected Client createClient(String targetIP, int targetPort, int connectTimeout, String key) throws Exception {
    if (isDebugEnabled) {
      LOGGER.debug("create connection to :" + targetIP + ":" + targetPort + ",timeout is:" + connectTimeout + ",key is:" + key);
    }
    if (connectTimeout > 1000) {
      ioConnector.setConnectTimeoutMillis((int) connectTimeout);
    } else {
      ioConnector.setConnectTimeoutMillis(1000);
    }
    SocketAddress targetAddress = new InetSocketAddress(targetIP, targetPort);
    MinaClientProcessor processor = new MinaClientProcessor(this, key);
    ioConnector.setHandler(processor);
    ConnectFuture connectFuture = ioConnector.connect(targetAddress);
    // wait for connection established
    connectFuture.awaitUninterruptibly();

    IoSession ioSession = connectFuture.getSession();
    if ((ioSession == null) || (!ioSession.isConnected())) {
      String targetUrl = targetIP + ":" + targetPort;
      LOGGER.error("create connection error,targetaddress is " + targetUrl);
      throw new Exception("create connection error,targetaddress is " + targetUrl);
    }
    if (isDebugEnabled) {
      LOGGER.debug(
          "create connection to :" + targetIP + ":" + targetPort + ",timeout is:" + connectTimeout + ",key is:" + key + " successed");
    }
    MinaClient client = new MinaClient(ioSession, key, connectTimeout);
    processor.setClient(client);
    return client;
  }

}
