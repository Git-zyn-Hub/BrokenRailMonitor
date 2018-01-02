package com.terrytec.brokenrailmonitor.password;

import com.terrytec.brokenrailmonitor.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

public class PasswordWindow extends PopupWindow {
	private LayoutInflater inflaterGlobal;
	// ����PopupWindow���������
	private PasswordWindow pwdWindow;

	/***
	 * ��ȡPopupWindowʵ��
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
	 * ����PopupWindow
	 */
	protected void initPasswordWindow() {
		// ��ȡ�Զ��岼���ļ�pop.xml����ͼ
		if (inflaterGlobal == null) {
			return;
		}
		View popupWindow_view = inflaterGlobal.inflate(R.layout.window_password, null, false);
		// ����PopupWindowʵ��,200,150�ֱ��ǿ�Ⱥ͸߶�
		pwdWindow = new PasswordWindow(popupWindow_view, 500, 400, true);
		// popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.layout_popupwindow_style01,
		// null));
		// ���ö���Ч��
		pwdWindow.setAnimationStyle(R.style.AnimationFade);
		// ��������ط���ʧ
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

		// pop.xml��ͼ����Ŀؼ�
		final Button btnCancel = (Button) popupWindow_view.findViewById(R.id.btnCancel);

		// pop.xml��ͼ����Ŀؼ��������¼�
		// ��
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// �Ի�����ʧ
					pwdWindow.dismiss();
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		});
	}
}
