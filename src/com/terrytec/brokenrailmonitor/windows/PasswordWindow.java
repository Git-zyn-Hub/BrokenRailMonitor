package com.terrytec.brokenrailmonitor.windows;

import com.terrytec.brokenrailmonitor.HomeFragment;
import com.terrytec.brokenrailmonitor.MainActivity;
import com.terrytec.brokenrailmonitor.R;

import java.io.UnsupportedEncodingException;

import com.terrytec.brokenrailmonitor.Enums.CommandType;
import com.terrytec.brokenrailmonitor.classes.SendDataPackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

public class PasswordWindow extends PopupWindow {
	private LayoutInflater inflaterGlobal;
	// 声明PopupWindow对象的引用
	private PasswordWindow pwdWindow;
	private HomeFragment homeFragment;
	private EditText etPassword;

	public void setLayoutInflater(LayoutInflater inflater) {
		inflaterGlobal = inflater;
	}

	/***
	 * 获取PasswordWindow实例
	 */
	public PasswordWindow getPasswordWindow() {
		if (null != pwdWindow) {
			return pwdWindow;
		} else {
			initPasswordWindow();
			return pwdWindow;
		}
	}

	public PasswordWindow() {

	}

	public PasswordWindow(View contentView, int width, int height, Boolean focusable) {
		super(contentView, width, height, focusable);
	}

	/**
	 * 创建PopupWindow
	 */
	protected void initPasswordWindow() {
		// 获取自定义布局文件pop.xml的视图
		if (inflaterGlobal == null) {
			return;
		}
		homeFragment = ((HomeFragment) MainActivity.getMainActivity().homeFragment);
		View popupWindow_view = inflaterGlobal.inflate(R.layout.window_password, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		pwdWindow = new PasswordWindow(popupWindow_view, 500, 400, true);
		etPassword = (EditText) popupWindow_view.findViewById(R.id.etPassword);
		pwdWindow.setEtPassword(etPassword);
		// popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.layout_popupwindow_style01,
		// null));
		// 设置动画效果
		pwdWindow.setAnimationStyle(R.style.AnimationFade);
		// 点击其他地方消失
		// popupWindow_view.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (pwdWindow != null && pwdWindow.isShowing()) {
		// pwdWindow.dismiss();
		// pwdWindow = null;
		// }
		// return false;
		// }
		// });

		// pop.xml视图里面的控件
		final Button btnCancel = (Button) popupWindow_view.findViewById(R.id.btnCancel);

		// pop.xml视图里面的控件触发的事件
		// 打开
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// 对话框消失
					pwdWindow.dismiss();
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		});

		final Button btnOK = (Button) popupWindow_view.findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (homeFragment != null) {
					if (!homeFragment.getIsConnect()) {
						Toast.makeText(MainActivity.getMainActivity(), "请先连接！", Toast.LENGTH_LONG).show();
						return;
					}
					byte[] bytesPwd;
					try {
						bytesPwd = etPassword.getText().toString().getBytes("UTF-8");
						homeFragment.sendBytesBuffer = SendDataPackage.PackageSendData(
								(byte) MainActivity.getMainActivity().ClientID, (byte) 0xff,
								(byte) CommandType.ConfigInitialInfoPassword.getValue(), bytesPwd);
						new Thread(homeFragment.sendBytesThread).start();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void setEtPassword(EditText value) {
		etPassword = value;
	}

	public void ClearPwdEditText() {
		etPassword.setText("");
	}
}
