package com.terrytec.brokenrailmonitor;

public class Enums extends Object {

	public enum RailStates {
		IsError, IsNormal, IsDifferent
	}

	public enum CommandType {
		RequestConfig(0xA0), AssignClientID(0xA1);

		private int value;

		// ������Ĭ��Ҳֻ����private, �Ӷ���֤���캯��ֻ�����ڲ�ʹ��
		CommandType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
		
		public static CommandType valueOf(int value) {    //    ��д�Ĵ�int��enum��ת������
	        switch (value) {
	        case 0xA0:
	            return RequestConfig;
	        case 0xA1:
	            return AssignClientID;
	        default:
	            return null;
	        }
	    }
	}

	public enum DataLevel {
		Default, Normal, Warning, Error
	}
}
