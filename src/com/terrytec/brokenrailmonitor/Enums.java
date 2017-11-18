package com.terrytec.brokenrailmonitor;

public class Enums extends Object {

	public enum RailStates {
		IsError, IsNormal, IsDifferent
	}

	public enum CommandType {
		RequestConfig(0xA0), AssignClientID(0xA1);

		private int value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		CommandType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
		
		public static CommandType valueOf(int value) {    //    手写的从int到enum的转换函数
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
