package com.bytesgo.nfs.rpc.core.protocol;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytesgo.nfs.rpc.core.NFSException;
import com.bytesgo.nfs.rpc.core.codec.Codecs;
import com.bytesgo.nfs.rpc.core.message.RequestMessage;
import com.bytesgo.nfs.rpc.core.message.ResponseMessage;

/**
 * Common RPC Protocol
 * 
 * Protocol Header VERSION(1B): Protocol Version TYPE(1B): Protocol Type,so u
 * can custom your protocol Request Protocol VERSION(1B): TYPE(1B):
 * request/response CODECTYPE(1B): serialize/deserialize type KEEPED(1B):
 * KEEPED(1B): KEEPED(1B): ID(4B): request id TIMEOUT(4B): request timeout
 * TARGETINSTANCELEN(4B): target service name length METHODNAMELEN(4B): method
 * name length ARGSCOUNT(4B): method args count ARG1TYPELEN(4B): method arg1
 * type len ARG2TYPELEN(4B): method arg2 type len ... ARG1LEN(4B): method arg1
 * len ARG2LEN(4B): method arg2 len ... TARGETINSTANCENAME METHODNAME
 * ARG1TYPENAME ARG2TYPENAME ... ARG1 ARG2 ...
 * 
 * Protocol Header VERSION(1B): Protocol Version TYPE(1B): Protocol Type,so u
 * can custom your protocol Response Protocol VERSION(1B): TYPE(1B):
 * request/response DATATYPE(1B): serialize/deserialize type KEEPED(1B):
 * KEEPED(1B): KEEPED(1B): ID(4B): request id BodyClassNameLen(4B): body
 * className Len LENGTH(4B): body length BodyClassName BODY if need than set
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class RPCProtocol implements Protocol {

  public static final int TYPE = 1;

  private static final Logger LOGGER = LoggerFactory.getLogger(RPCProtocol.class);

  private static final int REQUEST_HEADER_LEN = 1 * 6 + 5 * 4;

  private static final int RESPONSE_HEADER_LEN = 1 * 6 + 3 * 4;

  private static final byte VERSION = (byte) 1;

  private static final byte REQUEST = (byte) 0;

  private static final byte RESPONSE = (byte) 1;

  public ByteBufferWrapper encode(Object message, ByteBufferWrapper bytebufferWrapper) throws ProtocolException {
    if (!(message instanceof RequestMessage) && !(message instanceof ResponseMessage)) {
      throw new NFSException("only support send RequestWrapper && ResponseWrapper");
    }
    int id = 0;
    byte type = REQUEST;
    if (message instanceof RequestMessage) {
      try {
        int requestArgTypesLen = 0;
        int requestArgsLen = 0;
        List<byte[]> requestArgTypes = new ArrayList<byte[]>();
        List<byte[]> requestArgs = new ArrayList<byte[]>();
        RequestMessage request = (RequestMessage) message;
        byte[][] requestArgTypeStrings = request.getArgTypes();
        for (byte[] requestArgType : requestArgTypeStrings) {
          requestArgTypes.add(requestArgType);
          requestArgTypesLen += requestArgType.length;
        }
        Object[] requestObjects = request.getArgs();
        if (requestObjects != null) {
          for (Object requestArg : requestObjects) {
            byte[] requestArgByte = Codecs.getEncoder(request.getCodecType()).encode(requestArg);
            requestArgs.add(requestArgByte);
            requestArgsLen += requestArgByte.length;
          }
        }
        byte[] targetInstanceNameByte = request.getTargetInstanceName();
        byte[] methodNameByte = request.getMethodName();
        id = request.getId();
        int timeout = request.getTimeout();
        int capacity = ProtocolUtils.HEADER_LEN + REQUEST_HEADER_LEN + requestArgs.size() * 4 * 2 + targetInstanceNameByte.length
            + methodNameByte.length + requestArgTypesLen + requestArgsLen;
        ByteBufferWrapper byteBuffer = bytebufferWrapper.get(capacity);
        byteBuffer.writeByte(ProtocolUtils.CURRENT_VERSION);
        byteBuffer.writeByte((byte) TYPE);
        byteBuffer.writeByte(VERSION);
        byteBuffer.writeByte(type);
        byteBuffer.writeByte((byte) request.getCodecType());
        byteBuffer.writeByte((byte) 0);
        byteBuffer.writeByte((byte) 0);
        byteBuffer.writeByte((byte) 0);
        byteBuffer.writeInt(id);
        byteBuffer.writeInt(timeout);
        byteBuffer.writeInt(targetInstanceNameByte.length);
        byteBuffer.writeInt(methodNameByte.length);
        byteBuffer.writeInt(requestArgs.size());
        for (byte[] requestArgType : requestArgTypes) {
          byteBuffer.writeInt(requestArgType.length);
        }
        for (byte[] requestArg : requestArgs) {
          byteBuffer.writeInt(requestArg.length);
        }
        byteBuffer.writeBytes(targetInstanceNameByte);
        byteBuffer.writeBytes(methodNameByte);
        for (byte[] requestArgType : requestArgTypes) {
          byteBuffer.writeBytes(requestArgType);
        }
        for (byte[] requestArg : requestArgs) {
          byteBuffer.writeBytes(requestArg);
        }
        return byteBuffer;
      } catch (Exception e) {
        LOGGER.error("encode request object error", e);
        throw new NFSException(e);
      }
    } else {
      ResponseMessage response = (ResponseMessage) message;
      byte[] body = new byte[0];
      byte[] className = new byte[0];
      try {
        // no return object
        if (response.getResponse() != null) {
          className = response.getResponse().getClass().getName().getBytes();
          body = Codecs.getEncoder(response.getCodecType()).encode(response.getResponse());
        }
        if (response.isError()) {
          className = response.getException().getClass().getName().getBytes();
          body = Codecs.getEncoder(response.getCodecType()).encode(response.getException());
        }
        id = response.getId();
      } catch (Exception e) {
        LOGGER.error("encode response object error", e);
        // still create responsewrapper,so client can get exception
        response.setResponse(new Exception("serialize response object error", e));
        className = Exception.class.getName().getBytes();
        body = Codecs.getEncoder(response.getCodecType()).encode(response.getResponse());
      }
      type = RESPONSE;
      int capacity = ProtocolUtils.HEADER_LEN + RESPONSE_HEADER_LEN + body.length + className.length;
      ByteBufferWrapper byteBuffer = bytebufferWrapper.get(capacity);
      byteBuffer.writeByte(ProtocolUtils.CURRENT_VERSION);
      byteBuffer.writeByte((byte) TYPE);
      byteBuffer.writeByte(VERSION);
      byteBuffer.writeByte(type);
      byteBuffer.writeByte((byte) response.getCodecType());
      byteBuffer.writeByte((byte) 0);
      byteBuffer.writeByte((byte) 0);
      byteBuffer.writeByte((byte) 0);
      byteBuffer.writeInt(id);
      byteBuffer.writeInt(className.length);
      byteBuffer.writeInt(body.length);
      byteBuffer.writeBytes(className);
      byteBuffer.writeBytes(body);
      return byteBuffer;
    }
  }

  public Object decode(ByteBufferWrapper wrapper, Object errorObject, int... originPosArray) throws ProtocolException {
    final int originPos;
    if (originPosArray != null && originPosArray.length == 1) {
      originPos = originPosArray[0];
    } else {
      originPos = wrapper.readerIndex();
    }
    if (wrapper.readableBytes() < 2) {
      wrapper.setReaderIndex(originPos);
      return errorObject;
    }
    byte version = wrapper.readByte();
    if (version == (byte) 1) {
      byte type = wrapper.readByte();
      if (type == REQUEST) {
        if (wrapper.readableBytes() < REQUEST_HEADER_LEN - 2) {
          wrapper.setReaderIndex(originPos);
          return errorObject;
        }
        int codecType = wrapper.readByte();
        wrapper.readByte();
        wrapper.readByte();
        wrapper.readByte();
        int requestId = wrapper.readInt();
        int timeout = wrapper.readInt();
        int targetInstanceLen = wrapper.readInt();
        int methodNameLen = wrapper.readInt();
        int argsCount = wrapper.readInt();
        int argInfosLen = argsCount * 4 * 2;
        int expectedLenInfoLen = argInfosLen + targetInstanceLen + methodNameLen;
        if (wrapper.readableBytes() < expectedLenInfoLen) {
          wrapper.setReaderIndex(originPos);
          return errorObject;
        }
        int expectedLen = 0;
        int[] argsTypeLen = new int[argsCount];
        for (int i = 0; i < argsCount; i++) {
          argsTypeLen[i] = wrapper.readInt();
          expectedLen += argsTypeLen[i];
        }
        int[] argsLen = new int[argsCount];
        for (int i = 0; i < argsCount; i++) {
          argsLen[i] = wrapper.readInt();
          expectedLen += argsLen[i];
        }
        byte[] targetInstanceByte = new byte[targetInstanceLen];
        wrapper.readBytes(targetInstanceByte);
        byte[] methodNameByte = new byte[methodNameLen];
        wrapper.readBytes(methodNameByte);
        if (wrapper.readableBytes() < expectedLen) {
          wrapper.setReaderIndex(originPos);
          return errorObject;
        }
        byte[][] argTypes = new byte[argsCount][];
        for (int i = 0; i < argsCount; i++) {
          byte[] argTypeByte = new byte[argsTypeLen[i]];
          wrapper.readBytes(argTypeByte);
          argTypes[i] = argTypeByte;
        }
        Object[] args = new Object[argsCount];
        for (int i = 0; i < argsCount; i++) {
          byte[] argByte = new byte[argsLen[i]];
          wrapper.readBytes(argByte);
          args[i] = argByte;
        }
        RequestMessage requestWrapper = new RequestMessage(targetInstanceByte, methodNameByte, argTypes, args, timeout, requestId,
            codecType, TYPE);
        int messageLen = ProtocolUtils.HEADER_LEN + REQUEST_HEADER_LEN + expectedLenInfoLen + expectedLen;
        requestWrapper.setMessageLen(messageLen);
        return requestWrapper;
      } else if (type == RESPONSE) {
        if (wrapper.readableBytes() < RESPONSE_HEADER_LEN - 2) {
          wrapper.setReaderIndex(originPos);
          return errorObject;
        }
        int codecType = wrapper.readByte();
        wrapper.readByte();
        wrapper.readByte();
        wrapper.readByte();
        int requestId = wrapper.readInt();
        int classNameLen = wrapper.readInt();
        int bodyLen = wrapper.readInt();
        if (wrapper.readableBytes() < classNameLen + bodyLen) {
          wrapper.setReaderIndex(originPos);
          return errorObject;
        }

        byte[] classNameBytes = new byte[classNameLen];
        wrapper.readBytes(classNameBytes);
        byte[] bodyBytes = new byte[bodyLen];
        wrapper.readBytes(bodyBytes);
        ResponseMessage responseWrapper = new ResponseMessage(requestId, codecType, TYPE);
        responseWrapper.setResponse(bodyBytes);
        responseWrapper.setResponseClassName(classNameBytes);
        int messageLen = ProtocolUtils.HEADER_LEN + RESPONSE_HEADER_LEN + classNameLen + bodyLen;
        responseWrapper.setMessageLen(messageLen);
        return responseWrapper;
      } else {
        throw new UnsupportedOperationException("protocol type : " + type + " is not supported!");
      }
    } else {
      throw new UnsupportedOperationException("protocol version :" + version + " is not supported!");
    }
  }

}
