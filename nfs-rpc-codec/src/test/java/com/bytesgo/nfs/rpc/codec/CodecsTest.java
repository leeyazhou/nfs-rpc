package com.bytesgo.nfs.rpc.codec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.bytesgo.nfs.rpc.codec.java.JavaCodec;

/**
 * Tests for the {@link Codecs} static registry.
 *
 * @author leeyazhou
 */
class CodecsTest {

	@Test
	void getCodec_validKeys_returnsRegisteredCodec() {
		assertThat(Codecs.getCodec(Codecs.JAVA_CODEC)).isNotNull();
		assertThat(Codecs.getCodec(Codecs.HESSIAN_CODEC)).isNotNull();
		assertThat(Codecs.getCodec(Codecs.PB_CODEC)).isNotNull();
		assertThat(Codecs.getCodec(Codecs.KRYO_CODEC)).isNotNull();
	}

	@Test
	void getCodec_negativeKey_returnsNull() {
		assertThat(Codecs.getCodec(-1)).isNull();
	}

	@Test
	void getCodec_outOfBoundsKey_returnsNull() {
		assertThat(Codecs.getCodec(Integer.MAX_VALUE)).isNull();
	}

	@Test
	void addCodec_nullCodec_accepted() {
		int key = 100;
		Codecs.addCodec(key, null);
		// null codec is stored as-is; retrieval returns null value
		assertThat(Codecs.getCodec(key)).isNull();
	}

	@Test
	void addCodec_negativeKey_throwsIllegalArgument() {
		assertThatThrownBy(() -> Codecs.addCodec(-5, new JavaCodec()))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining(">= 0");
	}

	@Test
	void addCodec_expandsArray() {
		int key = 200;
		Codec dummy = new JavaCodec();
		Codecs.addCodec(key, dummy);
		assertThat(Codecs.getCodec(key)).isSameAs(dummy);
		// existing codecs should still be present
		assertThat(Codecs.getCodec(Codecs.HESSIAN_CODEC)).isNotNull();
	}
}
