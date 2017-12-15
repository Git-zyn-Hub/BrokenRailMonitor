package com.terrytec.brokenrailmonitor.classes;

public class SendDataPackage {
	private static final byte _frameHeader1 = 0x55;
	private static final byte _frameHeader2 = (byte) 0xAA;

	private static byte _checksum = 0;

	public SendDataPackage() {

	}

	public static byte[] PackageSendData(byte sourceAddr, byte destinationAddr, byte dataType, byte[] dataContent) {
		byte[] result;
		int length = 0;
		length = 7 + dataContent.length;
		result = new byte[length];
		result[0] = _frameHeader1;
		result[1] = _frameHeader2;
		result[2] = (byte) length;
		result[3] = sourceAddr;
		result[4] = destinationAddr;
		result[5] = dataType;
		for (int i = 0; i < dataContent.length; i++) {
			result[6 + i] = dataContent[i];
		}
		_checksum = 0;
		for (int i = 0; i < length - 1; i++) {
			_checksum += (result[i] & 0xFF);
		}
		result[length - 1] = _checksum;
		return result;
	}
}
