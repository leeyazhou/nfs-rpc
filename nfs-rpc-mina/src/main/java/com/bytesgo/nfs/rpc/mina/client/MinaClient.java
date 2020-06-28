package com.bytesgo.nfs.rpc.mina.client;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.net.InetSocketAddress;

import org.apache.mina.common.IoFuture;
import org.apache.mina.common.IoFutureListener;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.common.RequestWrapper;
import com.bytesgo.nfs.rpc.common.ResponseWrapper;
import com.bytesgo.nfs.rpc.common.client.AbstractClient;
import com.bytesgo.nfs.rpc.common.client.Client;
import com.bytesgo.nfs.rpc.common.client.ClientFactory;

/**
 * Mina Client
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaClient extends AbstractClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(MinaClient.class);

  private static final boolean isWarnEnabled = LOGGER.isWarnEnabled();

  private IoSession session;

  private String key;

  private int connectTimeout;

  public MinaClient(IoSession session, String key, int connectTimeout) {
    this.session = session;
    this.key = key;
    this.connectTimeout = connectTimeout;
  }

  public void sendRequest(final RequestWrapper wrapper, final int timeout) throws Exception {
    final long beginTime = System.currentTimeMillis();
    WriteFuture writeFuture = session.write(wrapper);
    final Client self = this;
    writeFuture.addListener(new IoFutureListener() {
      public void operationComplete(IoFuture future) {
        WriteFuture wfuture = (WriteFuture) future;
        if (wfuture.isWritten()) {
          return;
        }
        String error = "send message to server: " + session.getRemoteAddress()
            + " error,maybe because sendbuffer is full or connection closed: " + !session.isConnected();
        if (System.currentTimeMillis() - beginTime >= timeout) {
          error = "write message to os send buffer timeout,consumetime is: " + (System.currentTimeMillis() - beginTime) + "ms,timeout is:"
              + timeout;
        }
        LOGGER.error(error);
        ResponseWrapper response = new ResponseWrapper(wrapper.getId(), wrapper.getCodecType(), wrapper.getProtocolType());
        response.setException(new Exception(error));
        try {
          putResponse(response);
        } catch (Exception e) {
          // IGNORE, should not happen
        }
        if (session.isConnected()) {
          if (isWarnEnabled) {
            LOGGER.warn("close the session because send request error,server:" + session.getRemoteAddress());
          }
          session.close();
        } else {
          // TODO: exception handle
          MinaClientFactory.getInstance().removeClient(key, self);
        }
      }
    });
  }

  public long getSendingBytesSize() {
    return session.getScheduledWriteBytes();
  }

  public String getServerIP() {
    return ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
  }

  public int getServerPort() {
    return ((InetSocketAddress) session.getRemoteAddress()).getPort();
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public ClientFactory getClientFactory() {
    return MinaClientFactory.getInstance();
  }

}
