package com.xyz.rpc.server;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyz.rpc.Codecs;
import com.xyz.rpc.RequestWrapper;
import com.xyz.rpc.ResponseWrapper;

/**
 * Direct Call RPC Server Handler
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class SimpleProcessorServerHandler implements ServerHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleProcessorServerHandler.class);

  private Map<String, ServerProcessor> processors = new ConcurrentHashMap<String, ServerProcessor>();

  public void registerProcessor(String instanceName, Object instance) {
    processors.put(instanceName, (ServerProcessor) instance);
  }

  public ResponseWrapper handleRequest(final RequestWrapper request) {
    ResponseWrapper responseWrapper = new ResponseWrapper(request.getId(), request.getCodecType(), request.getProtocolType());
    try {
      String argType = null;
      if (request.getArgTypes() != null && request.getArgTypes()[0] != null) {
        argType = new String(request.getArgTypes()[0]);
      }
      Object requestObject = Codecs.getDecoder(request.getCodecType()).decode(argType, (byte[]) request.getMessage());
      responseWrapper.setResponse(processors.get(requestObject.getClass().getName()).handle(requestObject));
    } catch (Exception e) {
      LOGGER.error("server direct call handler to handle request error", e);
      responseWrapper.setException(e);
    }
    return responseWrapper;
  }
}
