package com.terrytec.brokenrailmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TerminalCmdActivity extends Activity {

	private CustomTitleBar ctbTerminalCmdTitle;
	private Button btnConfigInitialInfo;
	private Button btnReadPointInfo;
	private Button btnGetPointRailInfo;
	private Button btnGetHistory;
	private int terminalNo;
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
		}
	};
	private OnClickListener btnReadPointInfoListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};
	private OnClickListener btnGetPointRailInfoListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};
	private OnClickListener btnGetHistoryListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};
}
