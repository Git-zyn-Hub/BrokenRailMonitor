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
}
