package com.bytesgo.nfs.rpc.common.server;

import com.bytesgo.nfs.rpc.common.RequestWrapper;
import com.bytesgo.nfs.rpc.common.ResponseWrapper;

/**
 * Server Handler interface,when server receive message,it will handle
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface ServerHandler {

  /**
   * register business handler,provide for Server
   */
  public void registerProcessor(String instanceName, Object instance);

  /**
   * handle the request
   */
  public ResponseWrapper handleRequest(final RequestWrapper request);

}