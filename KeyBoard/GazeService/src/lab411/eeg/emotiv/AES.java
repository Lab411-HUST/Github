package lab411.eeg.emotiv;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	private static String algorithm = "AES";
	private static String algo_mode_padd = "AES/ECB/NoPadding";
	// private static byte[] keyValue = new byte[] { 51, 0, 53, 84, 53, 16, 49,
	// 66, 51, 0, 53, 72, 53, 0, 49, 80 }; // key emotiv generate
	// private static byte[] keyValue = new
	// byte[]{48,0,52,84,53,16,51,66,48,0,52,72,53,0,51,80};

	private static byte[] keyValue = new byte[] { 48, 0, 52, 84, 53, 16, 51,
			66, 48, 0, 52, 72, 53, 0, 51, 80 }; // Key Lab411

	private static int[] F3_MASK = new int[] { 10, 11, 12, 13, 14, 15, 0, 1, 2,
			3, 4, 5, 6, 7 };
	private static int[] FC6_MASK = new int[] { 214, 215, 200, 201, 202, 203,
			204, 205, 206, 207, 192, 193, 194, 195 };
	private static int[] P7_MASK = new int[] { 84, 85, 86, 87, 72, 73, 74, 75,
			76, 77, 78, 79, 64, 65 };
	private static int[] T8_MASK = new int[] { 160, 161, 162, 163, 164, 165,
			166, 167, 152, 153, 154, 155, 156, 157 };
	private static int[] F7_MASK = new int[] { 48, 49, 50, 51, 52, 53, 54, 55,
			40, 41, 42, 43, 44, 45 };
	private static int[] F8_MASK = new int[] { 178, 179, 180, 181, 182, 183,
			168, 169, 170, 171, 172, 173, 174, 175 };
	private static int[] T7_MASK = new int[] { 66, 67, 68, 69, 70, 71, 56, 57,
			58, 59, 60, 61, 62, 63 };
	private static int[] P8_MASK = new int[] { 158, 159, 144, 145, 146, 147,
			148, 149, 150, 151, 136, 137, 138, 139 };
	private static int[] AF4_MASK = new int[] { 196, 197, 198, 199, 184, 185,
			186, 187, 188, 189, 190, 191, 176, 177 };
	private static int[] F4_MASK = new int[] { 216, 217, 218, 219, 220, 221,
			222, 223, 208, 209, 210, 211, 212, 213 };
	private static int[] AF3_MASK = new int[] { 46, 47, 32, 33, 34, 35, 36, 37,
			38, 39, 24, 25, 26, 27 };
	private static int[] O2_MASK = new int[] { 140, 141, 142, 143, 128, 129,
			130, 131, 132, 133, 134, 135, 120, 121 };
	private static int[] O1_MASK = new int[] { 102, 103, 88, 89, 90, 91, 92,
			93, 94, 95, 80, 81, 82, 83 };
	private static int[] FC5_MASK = new int[] { 28, 29, 30, 31, 16, 17, 18, 19,
			20, 21, 22, 23, 8, 9 };
	private static int[] QUALITY_MASK = new int[] { 99, 100, 101, 102, 103,
			104, 105, 106, 107, 108, 109, 110, 111, 112 };

	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, algorithm);
		return key;
	}

	public static byte[] encrypt(byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance(algo_mode_padd);
		cipher.init(Cipher.ENCRYPT_MODE, generateKey());
		byte[] encrypted = cipher.doFinal(data);
		return encrypted;
	}

	public static byte[] decrypt(byte[] encrypt_data) throws Exception {
		Cipher cipher = Cipher.getInstance(algo_mode_padd);
		cipher.init(Cipher.DECRYPT_MODE, generateKey());
		byte[] decrypted = cipher.doFinal(encrypt_data);
		return decrypted;
	}

	public static byte[] get_raw_unenc(byte[] frame) throws Exception {
		byte[] raw_res = new byte[32];
		byte[] res = new byte[16];
		int i, j;
		byte[] raw_data = new byte[16];
		for (i = 0; i < 16; i++) {
			raw_data[i] = frame[i];
		}
		res = decrypt(raw_data);
		for (i = 0; i < 16; i++) {
			raw_res[i] = res[i];
		}
		for (i = 16, j = 0; i < 32; i++, j++) {
			raw_data[j] = frame[i];
		}
		res = decrypt(raw_data);
		for (i = 16, j = 0; i < 32; i++, j++) {
			raw_res[i] = res[j];
		}
		return raw_res;
	}

	public static int get_level(byte[] raw_unenc, int[] mask) {
		int i = 0;
		int b, o;
		int level = 0;
		for (i = 13; i >= 0; --i) {
			level <<= 1;
			b = (mask[i] / 8) + 1;
			o = mask[i] % 8;
			level |= (raw_unenc[b] >> o) & 1;
		}
		return level;
	}

	public static Emokit_Frame get_data(byte[] raw_encrypt_data) {
		Emokit_Frame k = new Emokit_Frame();
		k.F3 = get_level(raw_encrypt_data, F3_MASK);
		k.FC6 = get_level(raw_encrypt_data, FC6_MASK);
		k.P7 = get_level(raw_encrypt_data, P7_MASK);
		k.T8 = get_level(raw_encrypt_data, T8_MASK);
		k.F7 = get_level(raw_encrypt_data, F7_MASK);
		k.F8 = get_level(raw_encrypt_data, F8_MASK);
		k.T7 = get_level(raw_encrypt_data, T7_MASK);
		k.P8 = get_level(raw_encrypt_data, P8_MASK);
		k.AF4 = get_level(raw_encrypt_data, AF4_MASK);
		k.F4 = get_level(raw_encrypt_data, F4_MASK);
		k.AF3 = get_level(raw_encrypt_data, AF3_MASK);
		k.O2 = get_level(raw_encrypt_data, O2_MASK);
		k.O1 = get_level(raw_encrypt_data, O1_MASK);
		k.FC5 = get_level(raw_encrypt_data, FC5_MASK);
		return k;
	}

	public static String asHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;
		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				strbuf.append("0");
			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}
		return strbuf.toString();
	}
}
