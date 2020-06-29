package com.bytesgo.nfs.rpc.core.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.codec.Codecs;
import com.bytesgo.nfs.rpc.core.message.RequestMessage;
import com.bytesgo.nfs.rpc.core.message.ResponseMessage;

/**
 * Direct Call RPC Server Handler
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class SimpleProcessorServerHandler implements ServerHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleProcessorServerHandler.class);

  private final ConcurrentMap<String, ServerProcessor> processors = new ConcurrentHashMap<String, ServerProcessor>();

  public void registerProcessor(String instanceName, Object instance) {
    processors.put(instanceName, (ServerProcessor) instance);
  }

  public ResponseMessage handleRequest(final RequestMessage request) {
    ResponseMessage responseWrapper = new ResponseMessage(request.getId(), request.getCodecType(), request.getProtocolType());
    try {
      String argType = null;
      if (request.getArgTypes() != null && request.getArgTypes()[0] != null) {
        argType = new String(request.getArgTypes()[0]);
      }
      Object requestObject = Codecs.getCodec(request.getCodecType()).decode(argType, (byte[]) request.getMessage());
      responseWrapper.setResponse(processors.get(requestObject.getClass().getName()).handle(requestObject));
    } catch (Exception e) {
      LOGGER.error("server direct call handler to handle request error", e);
      responseWrapper.setException(e);
    }
    return responseWrapper;
  }
}
