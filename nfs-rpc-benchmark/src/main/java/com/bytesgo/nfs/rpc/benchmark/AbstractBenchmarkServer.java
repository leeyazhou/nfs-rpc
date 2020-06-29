package com.bytesgo.nfs.rpc.benchmark;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bytesgo.nfs.rpc.benchmark.service.BenchmarkServiceImpl;
import com.bytesgo.nfs.rpc.benchmark.service.object.RequestObject;
import com.bytesgo.nfs.rpc.benchmark.service.object.ResponseObject;
import com.bytesgo.nfs.rpc.benchmark.service.BenchmarkPBServiceImpl;
import com.bytesgo.nfs.rpc.core.codec.kryo.KryoUtils;
import com.bytesgo.nfs.rpc.core.codec.protobuf.ProtobufDecoder;
import com.bytesgo.nfs.rpc.core.protocol.RPCProtocol;
import com.bytesgo.nfs.rpc.core.protocol.SimpleProcessorProtocol;
import com.bytesgo.nfs.rpc.core.server.Server;
import com.bytesgo.nfs.rpc.core.server.ServerConfig;
import com.bytesgo.nfs.rpc.core.server.ServerProcessor;
import com.bytesgo.nfs.rpc.core.util.concurrent.NamedThreadFactory;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers;
import com.google.protobuf.ByteString;

/**
 * Abstract benchmark server
 * 
 * Usage: BenchmarkServer listenPort maxThreads responseSize
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public abstract class AbstractBenchmarkServer {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void run(String[] args) throws Exception {
		if (args == null || args.length != 3) {
			throw new IllegalArgumentException("must give three args: listenPort | maxThreads | responseSize");
		}
		int listenPort = Integer.parseInt(args[0]);
		int maxThreads = Integer.parseInt(args[1]);
		final int responseSize = Integer.parseInt(args[2]);
		System.out.println(dateFormat.format(new Date()) + " ready to start server,listenPort : " + listenPort
				+ ",maxThreads : " + maxThreads + ", responseSize : " + responseSize + " bytes");
		ServerConfig serverConfig = new ServerConfig();
		ExecutorService threadPool = new ThreadPoolExecutor(20, maxThreads, 300, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(), new NamedThreadFactory("worker"));
		serverConfig.setBusinessThreadPool(threadPool).setHost("127.0.0.1").setPort(listenPort);
		
		Server server = getServer(serverConfig);
		server.registerProcessor(SimpleProcessorProtocol.TYPE, RequestObject.class.getName(), new ServerProcessor() {
			public Object handle(Object request) throws Exception {
				return new ResponseObject(responseSize);
			}
		});
		// for pb codec
		ProtobufDecoder.addMessage(PB.RequestObject.class.getName(), PB.RequestObject.getDefaultInstance());
		ProtobufDecoder.addMessage(PB.ResponseObject.class.getName(), PB.ResponseObject.getDefaultInstance());
		server.registerProcessor(SimpleProcessorProtocol.TYPE, PB.RequestObject.class.getName(), new ServerProcessor() {
			public Object handle(Object request) throws Exception {
				PB.ResponseObject.Builder builder = PB.ResponseObject.newBuilder();
				builder.setBytesObject(ByteString.copyFrom(new byte[responseSize]));
				return builder.build();
			}
		});
		server.registerProcessor(RPCProtocol.TYPE, "testservice", new BenchmarkServiceImpl(responseSize));
		server.registerProcessor(RPCProtocol.TYPE, "testservicepb", new BenchmarkPBServiceImpl(responseSize));
		KryoUtils.registerClass(byte[].class, new DefaultArraySerializers.ByteArraySerializer(), 0);
		KryoUtils.registerClass(RequestObject.class, new RequestObjectSerializer(), 1);
		KryoUtils.registerClass(ResponseObject.class, new ResponseObjectSerializer(), 2);

		
		server.start();
	}

	/**
	 * Get server instance
	 */
	public abstract Server getServer(ServerConfig serverConfig);

}
