package com.example.binary;

public class AddBinary {

	public static void addBinary() {
		String b1 = "0";
		String b2 = "0";

		System.out.println(addBinary(b1, b2));
	}

	public static String addBinary(String b1, String b2) {
		return convertDeximalToBinary(convertBinaryToDeximal(b1)
				+ convertBinaryToDeximal(b2));
	}

	public static int convertBinaryToDeximal(String binaryNumber) {

		int strLength = binaryNumber.length();
		int deximal = 0;

		for (int i = 0; i < strLength; i++) {
			deximal = deximal + (binaryNumber.charAt(strLength - i - 1) - '0')
					* (int) Math.pow(2, i);
		}

		return deximal;
	}

	public static String convertDeximalToBinary(int deximal) {

		String binary = "";
		do {
			binary = String.valueOf(deximal % 2) + binary;
			deximal = deximal / 2;
		} while (deximal > 0);

		return binary;
	}
}
