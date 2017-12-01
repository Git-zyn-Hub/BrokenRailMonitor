package com.terrytec.brokenrailmonitor;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.terrytec.brokenrailmonitor.Enums.CommandType;
import com.terrytec.brokenrailmonitor.Enums.DataLevel;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

public class CommandFragment extends Fragment {

	private LinearLayout llCommandContainer;
	private View vTabCommand;
	private CustomTitleBar ctbCommandTitle;
	private ScrollView scvCommand;
	private Boolean stopScroll = false;
	private OnClickListener btnClearListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (llCommandContainer != null) {
				llCommandContainer.removeAllViews();
			}
		}
	};
	private OnClickListener btnFixListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (stopScroll) {
				stopScroll = false;
				ctbCommandTitle.getTitleBarRightBtn().setText("固定");
			} else {
				stopScroll = true;
				ctbCommandTitle.getTitleBarRightBtn().setText("不固定");
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("zyn", this.getClass().getName() + " onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("zyn", this.getClass().getName() + " onCreateView");
		vTabCommand = inflater.inflate(R.layout.tabcommand, container, false);
		llCommandContainer = (LinearLayout) vTabCommand.findViewById(R.id.llCommandContainer);
		ctbCommandTitle = (CustomTitleBar) vTabCommand.findViewById(R.id.ctbCommandTitle);
		ctbCommandTitle.setTitleLeftBtnClickListener(btnClearListener);
		ctbCommandTitle.setTitleRightBtnClickListener(btnFixListener);
		scvCommand = (ScrollView) vTabCommand.findViewById(R.id.scvCommand);
		return vTabCommand;
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e("zyn", this.getClass().getName() + " onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e("zyn", this.getClass().getName() + " onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("zyn", this.getClass().getName() + " onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e("zyn", this.getClass().getName() + " onDestroyView");
	}

	public void AddCmdMsg(byte[] data, DataLevel level) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		TextView txtNewMsg = new TextView(MainActivity.getMainActivity());
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		String str_time = sdf.format(date);

		String str = null;
		String msg = null;
		if (data.length > 1) {
			if ((data[0] == 0x55 && (data[1] & 0xFF) == 0xAA) || (data[0] == 0x66 && (data[1] & 0xFF) == 0xCC))
				str = bytesToHexString(data, data.length);
			else {
				try {
					str = new String(data, "UTF-8").trim();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			msg = getMsg(str_time, str, getMsgInfo(data));
			level = getDataLevel(data);
		} else if (data.length == 0) {
			msg = getMsg(str_time, "与服务器断开连接！", null);
			level = DataLevel.Error;
		}
		switch (level) {
		case Default:
			txtNewMsg.setTextColor(getResources().getColor(R.color.black));
			break;
		case Normal:
			txtNewMsg.setTextColor(getResources().getColor(R.color.green));
			break;
		case Warning:
			txtNewMsg.setTextColor(getResources().getColor(R.color.orange));
			break;
		case Error:
			txtNewMsg.setTextColor(getResources().getColor(R.color.red));
			break;
		default:
			txtNewMsg.setTextColor(getResources().getColor(R.color.black));
			break;
		}

		txtNewMsg.setText(msg);
		txtNewMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		llCommandContainer.addView(txtNewMsg, lp);// 动态改变布局
		scrollControl();
	}

	private String getMsg(String time, String data, String info) {
		StringBuilder sb = new StringBuilder();
		sb.append(time);
		sb.append(" -> ");
		sb.append(data);
		if (info != null) {
			sb.append(":");
			sb.append(info);
		}
		return sb.toString();
	}

	public static String bytesToHexString(byte[] src, int count) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < count; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	private String getMsgInfo(byte[] data) {

		if (data.length > 6 && data[0] == 0x55 && (data[1] & 0xFF) == 0xAA) {
			switch (CommandType.valueOf(data[5] & 0xFF)) {
			case AssignClientID:
				return "为手机用户ID赋值" + data[4];
			case RequestConfig:
				return "请求终端配置文件";
			case UploadConfig:
				return "上传终端配置文件";
			case ReadPointInfo:
				return "读取单点配置信息";
			case GetPointRailInfo:
				return "获取单点铁轨信息";
			case ConfigInitialInfo:
				return "初始信息配置";
			case ThresholdSetting:
				return "设置门限";
			case GetHistory:
				return "获取历史信息";
			case ImmediatelyRespond:
				return "立即响应";
			case RealTimeConfig:
				return "实时时钟配置";
			case GetOneSectionInfo:
				return "获取某段铁轨信息";
			case EraseFlash:
				return "擦除flash";
			case ErrorReport:
				return "";
			default:
				return null;
			}
		} else if (data.length > 7 && data[0] == 0x66 && (data[1] & 0xFF) == 0xCC) {
			switch (CommandType.valueOf(data[6] & 0xFF)) {
			case AssignClientID:
				return "为手机用户ID赋值" + data[4];
			case RequestConfig:
				return "请求终端配置文件";
			case UploadConfig:
				return "上传终端配置文件";
			case ReadPointInfo:
				return "读取单点配置信息";
			case GetPointRailInfo:
				return "获取单点铁轨信息";
			case ConfigInitialInfo:
				return "初始信息配置";
			case ThresholdSetting:
				return "设置门限";
			case GetHistory:
				return "获取历史信息";
			case ImmediatelyRespond:
				switch (CommandType.valueOf(data[7] & 0xFF)) {
				case ConfigInitialInfo:
					return "初始信息配置指令，" + String.valueOf(data[4] & 0xFF) + "号4G终端已接收！";
				case ReadPointInfo:
					return "读取单点配置信息指令，" + String.valueOf(data[4] & 0xFF) + "号4G终端已接收！";
				case ThresholdSetting:
					return "设置门限指令，" + String.valueOf(data[4] & 0xFF) + "号4G终端已接收！";
				case RealTimeConfig:
					return "实时时钟配置指令，" + String.valueOf(data[4] & 0xFF) + "号4G终端已接收！";
				case GetHistory:
					return "获取Flash里存储的铁轨历史信息指令，" + String.valueOf(data[4] & 0xFF) + "号4G终端已接收！";
				case GetPointRailInfo:
					return "获取单点铁轨信息指令，" + String.valueOf(data[4] & 0xFF) + "号4G终端已接收！";
				case EraseFlash:
					return "擦除flash指令，" + String.valueOf(data[4] & 0xFF) + "号4G终端已接收！";
				case GetOneSectionInfo:
					return "获取某段铁轨信息，" + String.valueOf(data[4] & 0xFF) + "号4G终端已接收！";
				default:
					return "未知指令被接收！";
				}
			case RealTimeConfig:
				return "实时时钟配置";
			case GetOneSectionInfo:
				return "获取某段铁轨信息";
			case EraseFlash:
				return "擦除flash";
			case ErrorReport:
				return String.valueOf(data[7] & 0xFF) + "号终端失联，未收到其返回的数据！";
			default:
				return null;
			}
		}
		return null;
	}

	private DataLevel getDataLevel(byte[] data) {
		if (data.length > 6 && data[0] == 0x55 && (data[1] & 0xFF) == 0xAA) {
			switch (CommandType.valueOf(data[5] & 0xFF)) {
			case AssignClientID:
			case ImmediatelyRespond:
				return DataLevel.Normal;
			case RequestConfig:
			case UploadConfig:
			case ReadPointInfo:
			case GetPointRailInfo:
			case ConfigInitialInfo:
			case ThresholdSetting:
			case GetHistory:
			case RealTimeConfig:
			case GetOneSectionInfo:
				return DataLevel.Default;
			case EraseFlash:
			case ErrorReport:
				return DataLevel.Error;
			default:
				return DataLevel.Default;
			}
		} else if (data.length > 7 && data[0] == 0x66 && (data[1] & 0xFF) == 0xCC) {
			switch (CommandType.valueOf(data[6] & 0xFF)) {
			case AssignClientID:
				return DataLevel.Normal;
			case RequestConfig:
			case UploadConfig:
			case ReadPointInfo:
			case GetPointRailInfo:
			case ConfigInitialInfo:
			case ThresholdSetting:
			case GetHistory:
			case RealTimeConfig:
			case GetOneSectionInfo:
				return DataLevel.Default;
			case ImmediatelyRespond:
				switch (CommandType.valueOf(data[7] & 0xFF)) {
				case ConfigInitialInfo:
				case ReadPointInfo:
				case ThresholdSetting:
				case RealTimeConfig:
				case GetHistory:
				case GetPointRailInfo:
				case EraseFlash:
				case GetOneSectionInfo:
				default:
					return DataLevel.Normal;
				}
			case EraseFlash:
			case ErrorReport:
				return DataLevel.Error;
			default:
				return DataLevel.Default;
			}
		}
		return DataLevel.Default;
	}

	private void scrollControl() {
		if (!stopScroll) {
			scvCommand.post(new Runnable() {
				@Override
				public void run() {
					scvCommand.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
		}
	}
}
