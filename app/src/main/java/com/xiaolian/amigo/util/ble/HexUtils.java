package com.xiaolian.amigo.util.ble;

public class HexUtils {
	public static String HexstrSum(String hexstr) {
		int sum = 0;
		for (int i = 0; i < hexstr.length() / 2; i++) {
			sum += Integer.valueOf(hexstr.substring(i * 2, (i + 1) * 2),16);
		}
		String hex= Integer.toHexString(0xFF&sum);
		if (hex.length()>2) {
			hex=hex.substring(hex.length()-3,hex.length()-1);
		}else if(hex.length()<2){
			hex="0"+hex;
		}
		return hex;
	}
	public static String HexstrSum2(String hexstr) {
		int sum = Integer.valueOf(hexstr.substring(0, 2),16);
		for (int i =1; i < hexstr.length() / 2; i++) {
			sum ^= Integer.valueOf(hexstr.substring(i * 2, (i + 1) * 2),16);
		}
		String hex= Integer.toHexString(sum);
		if (hex.length()>2) {
			hex=hex.substring(hex.length()-3,hex.length()-1);
		}else if(hex.length()<2){
			hex="0"+hex;
		}
		return hex;
	}

	public static String moneyToHexstr(Double money) {
		int string = (int) (money * 100);
		String hex = Integer.toHexString(0XFFFF & string);
		if (hex.length() >= 4) {

		} else if (hex.length() == 3) {
			hex = "0" + hex;
		} else if (hex.length() == 2) {
			hex = "00" + hex;
		} else {
			hex = "000" + hex;
		}
		return hex;
	}
	public static String positionToHexstr(int position) {
		String hex = Integer.toHexString(position);
		if (hex.length() == 3) {
			hex = "0" + hex;
		} else if (hex.length() == 2) {
			hex = "00" + hex;
		} else if (hex.length() == 1) {
			hex = "000" + hex;
		}
		return hex;
	}

}
