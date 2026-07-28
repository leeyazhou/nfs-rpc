package com.bytesgo.nfs.rpc.core.protocol;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.bytesgo.nfs.rpc.core.message.RequestMessage;
import com.bytesgo.nfs.rpc.core.message.ResponseMessage;
import com.bytesgo.nfs.rpc.core.server.ServerHandler;

/**
 * Tests for {@link ProtocolFactory} static registry.
 *
 * @author leeyazhou
 */
class ProtocolFactoryTest {

	@Test
	void getProtocol_validType_returnsProtocol() {
		assertThat(ProtocolFactory.getProtocol(RPCProtocol.TYPE)).isNotNull();
		assertThat(ProtocolFactory.getProtocol(SimpleProcessorProtocol.TYPE)).isNotNull();
	}

	@Test
	void getProtocol_negativeType_returnsNull() {
		assertThat(ProtocolFactory.getProtocol(-1)).isNull();
	}

	@Test
	void getProtocol_outOfBoundsType_returnsNull() {
		assertThat(ProtocolFactory.getProtocol(Integer.MAX_VALUE)).isNull();
	}

	@Test
	void getServerHandler_validType_returnsHandler() {
		assertThat(ProtocolFactory.getServerHandler(RPCProtocol.TYPE)).isNotNull();
		assertThat(ProtocolFactory.getServerHandler(SimpleProcessorProtocol.TYPE)).isNotNull();
	}

	@Test
	void getServerHandler_negativeType_returnsNull() {
		assertThat(ProtocolFactory.getServerHandler(-1)).isNull();
	}

	@Test
	void getServerHandler_outOfBoundsType_returnsNull() {
		assertThat(ProtocolFactory.getServerHandler(Integer.MAX_VALUE)).isNull();
	}

	@Test
	void registerProtocol_negativeType_throwsIllegalArgument() {
		assertThatThrownBy(() -> ProtocolFactory.registerProtocol(-5, new RPCProtocol(), null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining(">= 0");
	}

	@Test
	void registerProtocol_validType_expandsArray() {
		int type = 100;
		RPCProtocol proto = new RPCProtocol();
		ProtocolFactory.registerProtocol(type, proto, null);

		assertThat(ProtocolFactory.getProtocol(type)).isSameAs(proto);
		assertThat(ProtocolFactory.getServerHandler(type)).isNull();
		// existing registrations remain intact
		assertThat(ProtocolFactory.getProtocol(RPCProtocol.TYPE)).isNotNull();
	}

	@Test
	void registerProtocol_validTypeWithHandler() {
		int type = 101;
		RPCProtocol proto = new RPCProtocol();
		StubHandler handler = new StubHandler();
		ProtocolFactory.registerProtocol(type, proto, handler);

		assertThat(ProtocolFactory.getProtocol(type)).isSameAs(proto);
		assertThat(ProtocolFactory.getServerHandler(type)).isSameAs(handler);
	}

	private static class StubHandler implements ServerHandler {
		@Override
		public void registerProcessor(String instanceName, Object instance) {
			// no-op
		}

		@Override
		public ResponseMessage handleRequest(RequestMessage request) {
			return null;
		}
	}
}
