/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
package com.bytesgo.nfs.rpc.benchmark.service;

import com.bytesgo.nfs.rpc.benchmark.PB.RequestObject;
import com.bytesgo.nfs.rpc.benchmark.service.object.ResponseObject;

/**
 * Just for Reflection RPC Benchmark
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class BenchmarkServiceImpl implements BenchmarkService {

  private int responseSize;

  public BenchmarkServiceImpl(int responseSize) {
    this.responseSize = responseSize;
  }

  // support java/hessian/pb codec
  public Object execute(Object request) {
    return new ResponseObject(responseSize);
  }

  public Object executePB(RequestObject request) {
    throw new UnsupportedOperationException("unsupported");
  }

}
