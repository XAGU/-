package com.xiaolian.amigo.util.ble;

import android.util.Log;

public class HexBytesUtils {

	private static final String qppHexStr = "0123456789ABCDEF";

	private static byte charToByte(char paramChar) {
		return (byte) "0123456789ABCDEF".indexOf(paramChar);
	}

	public static byte[] hexStr2Bytes(String paramString) {
		if (paramString.length() % 2 == 1) {
			paramString = "0" + paramString;
		}
		byte[] arrayOfByte = null;
		if (paramString != null) {
			if (paramString.isEmpty()) {
				return null;
			}
			String str = paramString.toUpperCase();
			int i = str.length() >> 1;
			char[] arrayOfChar = str.toCharArray();
			int j = 0;
			Log.i("QnDbg", "hexString.length() : " + str.length());
			do {
				int k = "0123456789ABCDEF".indexOf(arrayOfChar[j]);
				arrayOfByte = null;
				if (k == -1)
					break;
				j++;
			} while (j < str.length());
			arrayOfByte = new byte[i];
			for (int m = 0; m < i; m++) {
				int n = m * 2;
				arrayOfByte[m] = ((byte) (charToByte(arrayOfChar[n]) << 4 | charToByte(arrayOfChar[(n + 1)])));
			}
		}
		return arrayOfByte;
	}

	public static String bytesToHexString(byte[] paramArrayOfByte) {
		StringBuilder localStringBuilder = new StringBuilder("");
		if ((paramArrayOfByte == null) || (paramArrayOfByte.length <= 0))
			return null;
		for (int i = 0;; i++) {
			if (i >= paramArrayOfByte.length)
				return localStringBuilder.toString();
			String str = Integer.toHexString(0xFF & paramArrayOfByte[i]);
			if (str.length() < 2)
				localStringBuilder.append(0);
			localStringBuilder.append(str);
		}
	}

}
