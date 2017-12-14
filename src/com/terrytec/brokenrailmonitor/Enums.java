package com.terrytec.brokenrailmonitor;

public class Enums extends Object {

	public enum RailStates {
		IsError, IsNormal, IsDifferent
	}

	public enum CommandType {
		RequestConfig(0xA0), AssignClientID(0xA1), UploadConfig(0xA2), SubscribeAllRailInfo(0xA3), ConfigInitialInfo(
				0xF0), ReadPointInfo(0xF1), ThresholdSetting(0xF2), GetHistory(0xF4), GetPointRailInfo(
						0xF5), ImmediatelyRespond(0xFE), RealTimeConfig(
								0x52), GetOneSectionInfo(0x55), EraseFlash(0x56), ErrorReport(0x88);
		private int value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		CommandType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static CommandType valueOf(int value) { // 手写的从int到enum的转换函数
			switch (value) {
			case 0xA0:
				return RequestConfig;
			case 0xA1:
				return AssignClientID;
			case 0xA2:
				return UploadConfig;
			case 0xA3:
				return SubscribeAllRailInfo;
			case 0xF0:
				return ConfigInitialInfo;
			case 0xF1:
				return ReadPointInfo;
			case 0xF2:
				return ThresholdSetting;
			case 0xF4:
				return GetHistory;
			case 0xF5:
				return GetPointRailInfo;
			case 0xFE:
				return ImmediatelyRespond;
			case 0x52:
				return RealTimeConfig;
			case 0x55:
				return GetOneSectionInfo;
			case 0x56:
				return EraseFlash;
			case 0x88:
				return ErrorReport;
			default:
				return null;
			}
		}
	}

	public enum DataLevel {
		Default, Normal, Warning, Error, Timeout, ContinuousInterference
	}
}
