package com.bytesgo.nfs.rpc.benchmark.netty4;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bytesgo.nfs.rpc.benchmark.AbstractBenchmarkServer;
import com.bytesgo.nfs.rpc.benchmark.PB;
import com.bytesgo.nfs.rpc.benchmark.RequestObject;
import com.bytesgo.nfs.rpc.benchmark.RequestObjectSerializer;
import com.bytesgo.nfs.rpc.benchmark.ResponseObject;
import com.bytesgo.nfs.rpc.benchmark.ResponseObjectSerializer;
import com.bytesgo.nfs.rpc.benchmark.service.BenchmarkTestServiceImpl;
import com.bytesgo.nfs.rpc.benchmark.service.PBBenchmarkTestServiceImpl;
import com.bytesgo.nfs.rpc.core.protocol.RPCProtocol;
import com.bytesgo.nfs.rpc.core.protocol.SimpleProcessorProtocol;
import com.bytesgo.nfs.rpc.core.protocol.codec.KryoUtils;
import com.bytesgo.nfs.rpc.core.protocol.codec.PBDecoder;
import com.bytesgo.nfs.rpc.core.server.Server;
import com.bytesgo.nfs.rpc.core.server.ServerProcessor;
import com.bytesgo.nfs.rpc.netty4.server.Netty4Server;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers;
import com.google.protobuf.ByteString;

/**
 * Netty4 RPC Benchmark Server
 * 
 * @author <a href="mailto:coderplay@gmail.com">Min Zhou</a>
 */
public class Netty4BenchmarkServer extends AbstractBenchmarkServer {
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private int concurrency = 1;

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      args = new String[3];
      args[0] = "12200";
      args[1] = "100"; // thread size
      args[2] = "100";
    }
    new Netty4BenchmarkServer().run(args);
  }

  public void run(String[] args) throws Exception { // $1 12200 100 100 > $2
    // 2>&1 &
    if (args == null || args.length != 3) {
      throw new IllegalArgumentException("must give three args: listenPort | maxThreads | responseSize");
    }
    int listenPort = Integer.parseInt(args[0]);
    concurrency = Integer.parseInt(args[1]);
    final int responseSize = Integer.parseInt(args[2]);
    System.out.println(dateFormat.format(new Date()) + " ready to start server,listenPort is: " + listenPort + ",threads num is:"
        + concurrency + ",responseSize is:" + responseSize + " bytes");

    Server server = getServer();
    server.registerProcessor(SimpleProcessorProtocol.TYPE, RequestObject.class.getName(), new ServerProcessor() {
      public Object handle(Object request) throws Exception {
        return new ResponseObject(responseSize);
      }
    });
    // for pb codec
    PBDecoder.addMessage(PB.RequestObject.class.getName(), PB.RequestObject.getDefaultInstance());
    PBDecoder.addMessage(PB.ResponseObject.class.getName(), PB.ResponseObject.getDefaultInstance());
    server.registerProcessor(SimpleProcessorProtocol.TYPE, PB.RequestObject.class.getName(), new ServerProcessor() {
      public Object handle(Object request) throws Exception {
        PB.ResponseObject.Builder builder = PB.ResponseObject.newBuilder();
        builder.setBytesObject(ByteString.copyFrom(new byte[responseSize]));
        return builder.build();
      }
    });
    server.registerProcessor(RPCProtocol.TYPE, "testservice", new BenchmarkTestServiceImpl(responseSize));
    server.registerProcessor(RPCProtocol.TYPE, "testservicepb", new PBBenchmarkTestServiceImpl(responseSize));
    // kryo codec
    KryoUtils.registerClass(byte[].class, new DefaultArraySerializers.ByteArraySerializer(), 0);
    KryoUtils.registerClass(RequestObject.class, new RequestObjectSerializer(), 1);
    KryoUtils.registerClass(ResponseObject.class, new ResponseObjectSerializer(), 2);

    server.start(listenPort, null);
  }

  public Server getServer() {
    return new Netty4Server(concurrency);
  }

}
