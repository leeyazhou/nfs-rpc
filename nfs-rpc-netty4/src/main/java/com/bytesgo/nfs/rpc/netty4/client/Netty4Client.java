package com.bytesgo.nfs.rpc.netty4.client;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.client.AbstractClient;
import com.bytesgo.nfs.rpc.core.client.Client;
import com.bytesgo.nfs.rpc.core.client.ClientFactory;
import com.bytesgo.nfs.rpc.core.message.RequestMessage;
import com.bytesgo.nfs.rpc.core.message.ResponseMessage;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * Netty4 Client
 * 
 * @author <a href="mailto:coderplay@gmail.com">Min Zhou</a>
 */
public class Netty4Client extends AbstractClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(Netty4Client.class);

  private ChannelFuture cf;

  private String key;

  private int connectTimeout;

  public Netty4Client(ChannelFuture cf, String key, int connectTimeout) {
    this.cf = cf;
    this.key = key;
    this.connectTimeout = connectTimeout;
  }

  public void sendRequest(final RequestMessage message, final int timeout) throws Exception {
    final long beginTime = System.currentTimeMillis();
    final Client self = this;
    ChannelFuture writeFuture = cf.channel().writeAndFlush(message);
    // use listener to avoid wait for write & thread context switch
    writeFuture.addListener(new ChannelFutureListener() {
      public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
          return;
        }
        String errorMsg = "";
        // write timeout
        if (System.currentTimeMillis() - beginTime >= timeout) {
          errorMsg = "write to send buffer consume too long time(" + (System.currentTimeMillis() - beginTime) + "),request id is:"
              + message.getId();
        }
        if (future.isCancelled()) {
          errorMsg = "Send request to " + cf.channel().toString() + " cancelled by user,request id is:" + message.getId();
        }
        if (!future.isSuccess()) {
          if (cf.channel().isActive()) {
            // maybe some exception,so close the channel
            cf.channel().close();
          } else {
            Netty4ClientFactory.getInstance().removeClient(key, self);
          }
          errorMsg = "Send request to " + cf.channel().toString() + " error" + future.cause();
        }
        LOGGER.error(errorMsg);
        ResponseMessage response = new ResponseMessage(message.getId(), message.getCodecType(), message.getProtocolType());
        response.setException(new Exception(errorMsg));
        self.putResponse(response);
      }
    });
  }

  public String getServerIP() {
    return ((InetSocketAddress) cf.channel().remoteAddress()).getHostName();
  }

  public int getServerPort() {
    return ((InetSocketAddress) cf.channel().remoteAddress()).getPort();
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public long getSendingBytesSize() {
    // TODO: implement it
    return 0;
  }

  public ClientFactory getClientFactory() {
    return Netty4ClientFactory.getInstance();
  }

}
