package com.bytesgo.nfs.rpc.core.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Verifies the exception hierarchy: all typed RPC exceptions extend
 * {@link NFSException}, and key fields are accessible.
 *
 * @author leeyazhou
 */
class ExceptionHierarchyTest {

	@Test
	void nfsException_extendsRuntimeException() {
		assertThat(RuntimeException.class).isAssignableFrom(NFSException.class);
	}

	@Test
	void rpcTimeoutException_extendsNFSException() {
		assertThat(NFSException.class).isAssignableFrom(RpcTimeoutException.class);
	}

	@Test
	void rpcTimeoutException_preservesFields() {
		RpcTimeoutException ex = new RpcTimeoutException(5000, "10.0.0.1:8080", 42);

		assertThat(ex.getTimeout()).isEqualTo(5000);
		assertThat(ex.getServerAddress()).isEqualTo("10.0.0.1:8080");
		assertThat(ex.getRequestId()).isEqualTo(42);
		assertThat(ex.getMessage()).contains("5000").contains("10.0.0.1:8080").contains("42");
	}

	@Test
	void rpcRejectException_extendsNFSException() {
		assertThat(NFSException.class).isAssignableFrom(RpcRejectException.class);
	}

	@Test
	void rpcRejectException_preservesFields() {
		RpcRejectException ex = new RpcRejectException(1024L, 512L);

		assertThat(ex.getSendingBytesSize()).isEqualTo(1024L);
		assertThat(ex.getThreshold()).isEqualTo(512L);
		assertThat(ex.getMessage()).contains("1024").contains("512");
	}

	@Test
	void rpcRemoteException_extendsNFSException() {
		assertThat(NFSException.class).isAssignableFrom(RpcRemoteException.class);
	}

	@Test
	void rpcDecodeException_extendsNFSException() {
		assertThat(NFSException.class).isAssignableFrom(RpcDecodeException.class);
	}

	@Test
	void rpcSendException_extendsNFSException() {
		assertThat(NFSException.class).isAssignableFrom(RpcSendException.class);
	}

	@Test
	void protocolException_extendsNFSException() {
		assertThat(NFSException.class).isAssignableFrom(ProtocolException.class);
	}

	@Test
	void nfsException_preservesCause() {
		Throwable root = new RuntimeException("root cause");
		NFSException ex = new NFSException("wrapper", root);

		assertThat(ex.getCause()).isSameAs(root);
		assertThat(ex.getMessage()).isEqualTo("wrapper");
	}
}
