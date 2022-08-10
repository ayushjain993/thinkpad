package io.uhha.coin.common.util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonHelper {

	public static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
	public static final String SHA256 = "SHA-256";
	public static final String MD5 = "MD5";

	public static byte[] digest(String algorithm, ByteBuffer target) {
		MessageDigest _digest;
		try {
			// 获得摘要算法的 MessageDigest 对象
			_digest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		// 使用指定的字节更新摘要
		_digest.update(target);
		// 获得密文
		return _digest.digest();
	}

	public static String toHexString(byte[] bytes, String separator) {
		StringBuilder _tmp = new StringBuilder(bytes.length * 3);

		if (bytes == null || bytes.length == 0) {
			return "";
		}
		append(bytes[0], _tmp);

		for (int _i = 1; _i < bytes.length; _i++) {
			append(bytes[_i], _tmp.append(separator));
		}
		return _tmp.toString();
	}

	private static void append(final int target, StringBuilder sb) {
		int _item = target;
		_item &= 0xff;
		sb.append(HEX_DIGITS[_item >> 4]);
		sb.append(HEX_DIGITS[_item & 15]);
	}

	public static void main(String[] args) {
		String _key = "1_8_2018.11.12";
		byte[] _result = digest(SHA256, ByteBuffer.wrap(_key.getBytes()));
		String _one = "56caef5ccd9fe6abc3fe0f784ecee2f9a5e266d625b27ce921298c4f6e276ed6";//toHexString(_result, "");
		System.out.println(_one);
		_result = digest(SHA256, ByteBuffer.wrap(("2018.11.16_"+_one+"_uhhaisgood").getBytes()));
		String _two = toHexString(_result, "");
		System.out.println(_two);
	}
}
