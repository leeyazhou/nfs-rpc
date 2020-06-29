/**
 * 
 */
package com.bytesgo.nfs.rpc.core.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author leeyazhou
 *
 */
public class RpcResult {

	private final CountDownLatch latch = new CountDownLatch(1);
	private Object result;

	public Object getResult(long timeout, TimeUnit unit) throws InterruptedException {
		latch.await(timeout, unit);
		return result;
	}

	public Object getResult() throws InterruptedException {
		latch.await();
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
		this.latch.countDown();
	}
}
