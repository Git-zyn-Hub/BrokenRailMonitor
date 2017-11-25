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
		// ��ȡ��Intent��Я��������
		Bundle bundle = intent.getExtras();
		// ��bundle���ݰ���ȡ������
		String str = bundle.getString("terminalNo");// getString()����ָ��key��ֵ
		if (str != null) {
			terminalNo = Integer.valueOf(str);
			ctbTerminalCmdTitle.getTitleBarTitle().setText("����" + str + "���ն�");
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
				// ����ͨ��ʹ��
				Intent intent = new Intent();
				// ���÷�����Ϣ
				intent.putExtra("back", "������");
				// ���÷������ͨ��ʹ��
				setIntent(intent);
				// ��ɣ��رյ�ǰҳ�沢����
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
