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
	// ����PopupWindow���������
	private PasswordWindow pwdWindow;
	private HomeFragment homeFragment;
	private EditText etPassword;

	public void setLayoutInflater(LayoutInflater inflater) {
		inflaterGlobal = inflater;
	}

	/***
	 * ��ȡPasswordWindowʵ��
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
	 * ����PopupWindow
	 */
	protected void initPasswordWindow() {
		// ��ȡ�Զ��岼���ļ�pop.xml����ͼ
		if (inflaterGlobal == null) {
			return;
		}
		homeFragment = ((HomeFragment) MainActivity.getMainActivity().homeFragment);
		View popupWindow_view = inflaterGlobal.inflate(R.layout.window_password, null, false);
		// ����PopupWindowʵ��,200,150�ֱ��ǿ�Ⱥ͸߶�
		pwdWindow = new PasswordWindow(popupWindow_view, 500, 400, true);
		etPassword = (EditText) popupWindow_view.findViewById(R.id.etPassword);
		pwdWindow.setEtPassword(etPassword);
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

		final Button btnOK = (Button) popupWindow_view.findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (homeFragment != null) {
					if (!homeFragment.getIsConnect()) {
						Toast.makeText(MainActivity.getMainActivity(), "�������ӣ�", Toast.LENGTH_LONG).show();
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
