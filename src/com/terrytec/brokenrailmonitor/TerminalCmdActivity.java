package com.terrytec.brokenrailmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TerminalCmdActivity extends Activity {

	private CustomTitleBar ctbTerminalCmdTitle;
//	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terminal_cmd);
		ctbTerminalCmdTitle = (CustomTitleBar) findViewById(R.id.ctbTerminalCmdTitle);
		ctbTerminalCmdTitle.setTitleLeftBtnClickListener(backListener);
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
}
