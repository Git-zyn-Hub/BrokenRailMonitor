package com.terrytec.brokenrailmonitor.password;

import com.terrytec.brokenrailmonitor.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

public class PasswordWindow extends PopupWindow {
	private LayoutInflater inflaterGlobal;
	// 声明PopupWindow对象的引用
	private PasswordWindow pwdWindow;

	/***
	 * 获取PopupWindow实例
	 */
	public void setLayoutInflater(LayoutInflater inflater) {
		inflaterGlobal = inflater;
	}

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
		View popupWindow_view = inflaterGlobal.inflate(R.layout.window_password, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		pwdWindow = new PasswordWindow(popupWindow_view, 500, 400, true);
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
	}
}
