package com.bytesgo.nfs.rpc.core.client;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.bytesgo.nfs.rpc.core.exception.NFSException;

/**
 * Tests for {@link ClientConfig#validate()}.
 *
 * @author leeyazhou
 */
class ClientConfigTest {

	@Test
	void validate_validConfig_passes() {
		ClientConfig config = new ClientConfig()
				.setHost("127.0.0.1")
				.setPort(8080);

		assertThatCode(config::validate).doesNotThrowAnyException();
	}

	@Test
	void validate_customTimeoutAndClientNums_passes() {
		ClientConfig config = new ClientConfig()
				.setHost("10.0.0.1")
				.setPort(9090)
				.setConnectTimeout(5000)
				.setClientNums(4);

		assertThatCode(config::validate).doesNotThrowAnyException();
	}

	@Test
	void validate_nullHost_throwsNFSException() {
		ClientConfig config = new ClientConfig().setPort(8080);

		assertThatThrownBy(config::validate)
				.isInstanceOf(NFSException.class)
				.hasMessageContaining("host");
	}

	@Test
	void validate_blankHost_throwsNFSException() {
		ClientConfig config = new ClientConfig()
				.setHost("  ")
				.setPort(8080);

		assertThatThrownBy(config::validate)
				.isInstanceOf(NFSException.class)
				.hasMessageContaining("host");
	}

	@Test
	void validate_portTooLow_throwsNFSException() {
		ClientConfig config = new ClientConfig()
				.setHost("127.0.0.1")
				.setPort(0);

		assertThatThrownBy(config::validate)
				.isInstanceOf(NFSException.class)
				.hasMessageContaining("Invalid port");
	}

	@Test
	void validate_portTooHigh_throwsNFSException() {
		ClientConfig config = new ClientConfig()
				.setHost("127.0.0.1")
				.setPort(70000);

		assertThatThrownBy(config::validate)
				.isInstanceOf(NFSException.class)
				.hasMessageContaining("Invalid port");
	}

	@Test
	void validate_negativeConnectTimeout_throwsNFSException() {
		ClientConfig config = new ClientConfig()
				.setHost("127.0.0.1")
				.setPort(8080)
				.setConnectTimeout(-1);

		assertThatThrownBy(config::validate)
				.isInstanceOf(NFSException.class)
				.hasMessageContaining("connectTimeout");
	}

	@Test
	void validate_zeroClientNums_throwsNFSException() {
		ClientConfig config = new ClientConfig()
				.setHost("127.0.0.1")
				.setPort(8080)
				.setClientNums(0);

		assertThatThrownBy(config::validate)
				.isInstanceOf(NFSException.class)
				.hasMessageContaining("clientNums");
	}

	@Test
	void defaults_areSensible() {
		ClientConfig config = new ClientConfig()
				.setHost("localhost")
				.setPort(8080);

		assertThatCode(config::validate).doesNotThrowAnyException();
		org.assertj.core.api.Assertions.assertThat(config.getConnectTimeout()).isEqualTo(3000);
		org.assertj.core.api.Assertions.assertThat(config.getClientNums()).isEqualTo(1);
		org.assertj.core.api.Assertions.assertThat(config.getCodecType()).isEqualTo(com.bytesgo.nfs.rpc.codec.Codecs.HESSIAN_CODEC);
	}
}
