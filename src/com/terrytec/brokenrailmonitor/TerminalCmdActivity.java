package com.terrytec.brokenrailmonitor;

import com.terrytec.brokenrailmonitor.Enums.CommandType;
import com.terrytec.brokenrailmonitor.classes.SendDataPackage;
import com.terrytec.brokenrailmonitor.windows.PasswordWindow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TerminalCmdActivity extends Activity {

	private CustomTitleBar ctbTerminalCmdTitle;
	private Button btnConfigInitialInfo;
	private Button btnReadPointInfo;
	private Button btnGetPointRailInfo;
	private Button btnGetHistory;
	private int terminalNo;
	private HomeFragment homeFragment;
	private PasswordWindow pwdWindow = new PasswordWindow();
	// private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terminal_cmd);
		ctbTerminalCmdTitle = (CustomTitleBar) findViewById(R.id.ctbTerminalCmdTitle);
		ctbTerminalCmdTitle.setTitleLeftBtnClickListener(backListener);

		btnConfigInitialInfo = (Button) findViewById(R.id.btnConfigInitialInfo);
		btnReadPointInfo = (Button) findViewById(R.id.btnReadPointInfo);
		btnGetPointRailInfo = (Button) findViewById(R.id.btnGetPointRailInfo);
		btnGetHistory = (Button) findViewById(R.id.btnGetHistory);
		setTitle();
		setBtnListener();
		homeFragment = ((HomeFragment) MainActivity.getMainActivity().homeFragment);
		homeFragment.CurrentActivity = this;
	}

	private void setTitle() {
		Intent intent = getIntent();
		// 获取该Intent所携带的数据
		Bundle bundle = intent.getExtras();
		// 从bundle数据包中取出数据
		String str = bundle.getString("terminalNo");// getString()返回指定key的值
		if (str != null) {
			terminalNo = Integer.valueOf(str);
			ctbTerminalCmdTitle.getTitleBarTitle().setText("操作" + str + "号终端");
		}
	}

	private void setBtnListener() {
		btnConfigInitialInfo.setOnClickListener(btnConfigInitialInfoListener);
		btnReadPointInfo.setOnClickListener(btnReadPointInfoListener);
		btnGetPointRailInfo.setOnClickListener(btnGetPointRailInfoListener);
		btnGetHistory.setOnClickListener(btnGetHistoryListener);
	}

	private OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			try {
				// 创建通信使者
				Intent intent = new Intent();
				// 设置返回信息
				intent.putExtra("back", "无数据");
				// 设置返回码和通信使者
				setIntent(intent);
				// 完成，关闭当前页面并返回
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private OnClickListener btnConfigInitialInfoListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				if (!homeFragment.getIsConnect()) {
					Toast.makeText(MainActivity.getMainActivity(), "请先连接！", Toast.LENGTH_LONG).show();
					return;
				}
				LayoutInflater inflater = getLayoutInflater();
				pwdWindow.setLayoutInflater(inflater);
				pwdWindow = pwdWindow.getPasswordWindow();
				pwdWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
				// homeFragment.sendBytesBuffer =
				// SendDataPackage.PackageSendData(
				// (byte) MainActivity.getMainActivity().ClientID, (byte) 0xff,
				// (byte) CommandType.RequestConfig.getValue(), new byte[] {
				// 0x48, 0x5f });
				// new Thread(homeFragment.sendBytesThread).start();
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	};
	private OnClickListener btnReadPointInfoListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!homeFragment.getIsConnect()) {
				Toast.makeText(MainActivity.getMainActivity(), "请先连接！", Toast.LENGTH_LONG).show();
				return;
			}
			homeFragment.sendBytesBuffer = SendDataPackage.PackageSendData((byte) 0xff, (byte) terminalNo,
					(byte) CommandType.ReadPointInfo.getValue(), new byte[] { (byte) terminalNo });
			new Thread(homeFragment.sendBytesThread).start();
		}
	};
	private OnClickListener btnGetPointRailInfoListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (!homeFragment.getIsConnect()) {
				Toast.makeText(MainActivity.getMainActivity(), "请先连接！", Toast.LENGTH_LONG).show();
				return;
			}
			homeFragment.sendBytesBuffer = SendDataPackage.PackageSendData((byte) 0xff, (byte) terminalNo,
					(byte) CommandType.GetPointRailInfo.getValue(), new byte[] { 0, 0 });
			new Thread(homeFragment.sendBytesThread).start();

		}
	};
	private OnClickListener btnGetHistoryListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};

	public PasswordWindow getPwdWindow() {
		return pwdWindow;
	}
}
