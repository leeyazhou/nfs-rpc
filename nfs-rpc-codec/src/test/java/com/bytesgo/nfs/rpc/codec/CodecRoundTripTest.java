package com.bytesgo.nfs.rpc.codec;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.bytesgo.nfs.rpc.codec.hessian.HessianCodec;
import com.bytesgo.nfs.rpc.codec.java.JavaCodec;
import com.bytesgo.nfs.rpc.codec.kryo.KryoCodec;

/**
 * Verifies that each built-in codec can encode and then decode an object
 * back to an equal instance.
 *
 * @author leeyazhou
 */
class CodecRoundTripTest {

	@Test
	void javaCodec_roundTrip_string() {
		Codec codec = new JavaCodec();
		String original = "hello nfs-rpc";

		byte[] encoded = codec.encode(original);
		Object decoded = codec.decode(String.class.getName(), encoded);

		assertThat(decoded).isEqualTo(original);
	}

	@Test
	void javaCodec_roundTrip_list() {
		Codec codec = new JavaCodec();
		List<String> original = Arrays.asList("a", "b", "c");

		byte[] encoded = codec.encode(original);
		Object decoded = codec.decode(List.class.getName(), encoded);

		assertThat(decoded).isEqualTo(original);
	}

	@Test
	void hessianCodec_roundTrip_string() {
		Codec codec = new HessianCodec();
		String original = "hello hessian";

		byte[] encoded = codec.encode(original);
		Object decoded = codec.decode(String.class.getName(), encoded);

		assertThat(decoded).isEqualTo(original);
	}

	@Test
	void hessianCodec_roundTrip_map() {
		Codec codec = new HessianCodec();
		Map<String, Integer> original = new HashMap<>();
		original.put("one", 1);
		original.put("two", 2);

		byte[] encoded = codec.encode(original);
		Object decoded = codec.decode(Map.class.getName(), encoded);

		assertThat(decoded).isEqualTo(original);
	}

	@Test
	void kryoCodec_roundTrip_string() {
		Codec codec = new KryoCodec();
		String original = "hello kryo";

		byte[] encoded = codec.encode(original);
		Object decoded = codec.decode(String.class.getName(), encoded);

		assertThat(decoded).isEqualTo(original);
	}

	@Test
	void kryoCodec_roundTrip_integer() {
		Codec codec = new KryoCodec();
		Integer original = Integer.valueOf(42);

		byte[] encoded = codec.encode(original);
		Object decoded = codec.decode(Integer.class.getName(), encoded);

		assertThat(decoded).isEqualTo(original);
	}

	@Test
	void javaCodec_roundTrip_null() {
		Codec codec = new JavaCodec();

		byte[] encoded = codec.encode(null);
		Object decoded = codec.decode(Object.class.getName(), encoded);

		assertThat(decoded).isNull();
	}

	@Test
	void decode_corruptBytes_throwsCodecException() {
		Codec codec = new JavaCodec();
		byte[] garbage = { 0x00, 0x01, 0x02, 0x03 };

		org.assertj.core.api.Assertions.assertThatCode(() -> codec.decode("java.lang.String", garbage))
				.isInstanceOf(CodecException.class);
	}
}
