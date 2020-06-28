/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
package com.bytesgo.nfs.rpc.benchmark.service;

import com.bytesgo.nfs.rpc.benchmark.PB;

/**
 * Just for Reflection RPC Benchmark
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface BenchmarkTestService {

	Object execute(Object request);

	Object executePB(PB.RequestObject request);

}
