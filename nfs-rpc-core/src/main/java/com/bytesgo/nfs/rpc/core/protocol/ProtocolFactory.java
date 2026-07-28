package com.bytesgo.nfs.rpc.core.protocol;

import com.bytesgo.nfs.rpc.core.server.RPCServerHandler;
import com.bytesgo.nfs.rpc.core.server.ServerHandler;
import com.bytesgo.nfs.rpc.core.server.SimpleProcessorServerHandler;

/**
 * Protocol Factory,for set Protocol class & serverHandler class
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class ProtocolFactory {

  private static volatile Protocol[] protocolHandlers = new Protocol[5];

  private static volatile ServerHandler[] serverHandlers = new ServerHandler[5];

  static {
    registerProtocol(RPCProtocol.TYPE, new RPCProtocol(), new RPCServerHandler());
    registerProtocol(SimpleProcessorProtocol.TYPE, new SimpleProcessorProtocol(), new SimpleProcessorServerHandler());
  }

  public static synchronized void registerProtocol(int type, Protocol customProtocol, ServerHandler customServerHandler) {
    if (type < 0) {
      throw new IllegalArgumentException("protocol type must be >= 0, got " + type);
    }
    if (type >= protocolHandlers.length) {
      Protocol[] newProtocolHandlers = new Protocol[type + 1];
      System.arraycopy(protocolHandlers, 0, newProtocolHandlers, 0, protocolHandlers.length);
      protocolHandlers = newProtocolHandlers;
      ServerHandler[] newServerHandlers = new ServerHandler[type + 1];
      System.arraycopy(serverHandlers, 0, newServerHandlers, 0, serverHandlers.length);
      serverHandlers = newServerHandlers;
    }
    protocolHandlers[type] = customProtocol;
    serverHandlers[type] = customServerHandler;
  }

  public static Protocol getProtocol(int type) {
    Protocol[] handlers = protocolHandlers;
    if (type < 0 || type >= handlers.length) {
      return null;
    }
    return handlers[type];
  }

  public static ServerHandler getServerHandler(int type) {
    ServerHandler[] handlers = serverHandlers;
    if (type < 0 || type >= handlers.length) {
      return null;
    }
    return handlers[type];
  }

}
