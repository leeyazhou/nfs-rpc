package com.bytesgo.nfs.rpc.core.server;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.bytesgo.nfs.rpc.core.exception.NFSException;

/**
 * Tests for {@link ServerConfig#validate()}.
 *
 * @author leeyazhou
 */
class ServerConfigTest {

	@Test
	void validate_validConfig_passes() {
		ServerConfig config = new ServerConfig()
				.setHost("127.0.0.1")
				.setPort(8080)
				.setMaxPoolSize(10);

		assertThatCode(config::validate).doesNotThrowAnyException();
	}

	@Test
	void validate_nullHost_passes() {
		ServerConfig config = new ServerConfig()
				.setPort(8080);

		assertThatCode(config::validate).doesNotThrowAnyException();
	}

	@Test
	void validate_portTooLow_throwsNFSException() {
		ServerConfig config = new ServerConfig().setPort(0);

		assertThatThrownBy(config::validate)
				.isInstanceOf(NFSException.class)
				.hasMessageContaining("Invalid port");
	}

	@Test
	void validate_portTooHigh_throwsNFSException() {
		ServerConfig config = new ServerConfig().setPort(65536);

		assertThatThrownBy(config::validate)
				.isInstanceOf(NFSException.class)
				.hasMessageContaining("Invalid port");
	}

	@Test
	void validate_blankHost_throwsNFSException() {
		ServerConfig config = new ServerConfig()
				.setHost("   ")
				.setPort(8080);

		assertThatThrownBy(config::validate)
				.isInstanceOf(NFSException.class)
				.hasMessageContaining("Host must not be blank");
	}

	@Test
	void validate_negativeMaxPoolSize_throwsNFSException() {
		ServerConfig config = new ServerConfig()
				.setPort(8080)
				.setMaxPoolSize(-1);

		assertThatThrownBy(config::validate)
				.isInstanceOf(NFSException.class)
				.hasMessageContaining("maxPoolSize");
	}

	@Test
	void validate_zeroMaxPoolSize_passes() {
		ServerConfig config = new ServerConfig()
				.setPort(8080)
				.setMaxPoolSize(0);

		assertThatCode(config::validate).doesNotThrowAnyException();
	}
}
