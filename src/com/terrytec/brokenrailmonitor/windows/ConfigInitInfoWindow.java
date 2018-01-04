package com.terrytec.brokenrailmonitor.windows;

import com.terrytec.brokenrailmonitor.HomeFragment;
import com.terrytec.brokenrailmonitor.MainActivity;
import com.terrytec.brokenrailmonitor.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

public class ConfigInitInfoWindow extends PopupWindow {

	private LayoutInflater inflaterGlobal;
	// ����PopupWindow���������
	private ConfigInitInfoWindow ciiWindow;
	private HomeFragment homeFragment;
	private EditText etNeighbourSmallSecondary;
	private EditText etNeighbourSmallPrimary;
	private EditText etThisTerminal;
	private EditText etNeighbourBigPrimary;
	private EditText etNeighbourBigSecondary;
	private int terminalNo;

	public ConfigInitInfoWindow() {
	}

	public ConfigInitInfoWindow(View contentView, int width, int height, boolean focusable) {
		super(contentView, width, height, focusable);
	}

	/***
	 * ��ȡConfigInitInfoWindowʵ��
	 */
	public ConfigInitInfoWindow getConfigInitInfoWindow() {
		if (null != ciiWindow) {
			return ciiWindow;
		} else {
			initConfigInitInfoWindow();
			return ciiWindow;
		}
	}

	public void setLayoutInflater(LayoutInflater inflater) {
		inflaterGlobal = inflater;
	}

	/**
	 * ����PopupWindow
	 */
	protected void initConfigInitInfoWindow() {
		// ��ȡ�Զ��岼���ļ�pop.xml����ͼ
		if (inflaterGlobal == null) {
			return;
		}
		homeFragment = ((HomeFragment) MainActivity.getMainActivity().homeFragment);
		View popupWindow_view = inflaterGlobal.inflate(R.layout.window_config_init_info, null, false);
		// ����PopupWindowʵ��,200,150�ֱ��ǿ��Ⱥ͸߶�
		ciiWindow = new ConfigInitInfoWindow(popupWindow_view, 700, 800, true);

		etNeighbourSmallSecondary = (EditText) popupWindow_view.findViewById(R.id.etNeighbourSmallSecondary);
		etNeighbourSmallPrimary = (EditText) popupWindow_view.findViewById(R.id.etNeighbourSmallPrimary);
		etThisTerminal = (EditText) popupWindow_view.findViewById(R.id.etThisTerminal);
		etNeighbourBigPrimary = (EditText) popupWindow_view.findViewById(R.id.etNeighbourBigPrimary);
		etNeighbourBigSecondary = (EditText) popupWindow_view.findViewById(R.id.etNeighbourBigSecondary);

		ciiWindow.setEtNeighbourSmallSecondary(etNeighbourSmallSecondary);
		ciiWindow.setEtNeighbourSmallPrimary(etNeighbourSmallPrimary);
		ciiWindow.setEtThisTerminal(etThisTerminal);
		ciiWindow.setEtNeighbourBigPrimary(etNeighbourBigPrimary);
		ciiWindow.setEtNeighbourBigSecondary(etNeighbourBigSecondary);

		setInitialInfo();
		// popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.layout_popupwindow_style01,
		// null));
		// ���ö���Ч��
		ciiWindow.setAnimationStyle(R.style.AnimationFade);
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
					ciiWindow.dismiss();
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		});

		final Button btnOK = (Button) popupWindow_view.findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
	}

	private void setInitialInfo() {
		if (homeFragment != null) {
			// ��ʼ��Ĭ�ϵ��ն˺š�
			int index = homeFragment.FindMasterControlIndex(terminalNo);
			ciiWindow.getEtThisTerminal().setText(String.valueOf(terminalNo));
			if (index == 0) {
				ciiWindow.getEtNeighbourSmallSecondary().setText("0");
				ciiWindow.getEtNeighbourSmallPrimary().setText("0");
			} else if (index == 1) {
				ciiWindow.getEtNeighbourSmallSecondary().setText("0");
				ciiWindow.getEtNeighbourSmallPrimary()
						.setText(String.valueOf(homeFragment.terminalAnd2Rails.get(0).terminalNo));
			} else {
				ciiWindow.getEtNeighbourSmallSecondary()
						.setText(String.valueOf(homeFragment.terminalAnd2Rails.get(index - 2).terminalNo));
				ciiWindow.getEtNeighbourSmallPrimary()
						.setText(String.valueOf(homeFragment.terminalAnd2Rails.get(index - 1).terminalNo));
			}
			if (index == homeFragment.terminalAnd2Rails.size() - 2) {
				ciiWindow.getEtNeighbourBigPrimary().setText(String.valueOf(
						homeFragment.terminalAnd2Rails.get(homeFragment.terminalAnd2Rails.size() - 1).terminalNo));
				ciiWindow.getEtNeighbourBigSecondary().setText("255");
			} else if (index == homeFragment.terminalAnd2Rails.size() - 1) {
				ciiWindow.getEtNeighbourBigPrimary().setText("255");
				ciiWindow.getEtNeighbourBigSecondary().setText("255");
			} else {
				ciiWindow.getEtNeighbourBigPrimary()
						.setText(String.valueOf(homeFragment.terminalAnd2Rails.get(index + 1).terminalNo));
				ciiWindow.getEtNeighbourBigSecondary()
						.setText(String.valueOf(homeFragment.terminalAnd2Rails.get(index + 2).terminalNo));
			}
		}
	}

	private void setEtNeighbourSmallSecondary(EditText value) {
		etNeighbourSmallSecondary = value;
	}

	private void setEtNeighbourSmallPrimary(EditText value) {
		etNeighbourSmallPrimary = value;
	}

	private void setEtThisTerminal(EditText value) {
		etThisTerminal = value;
	}

	private void setEtNeighbourBigPrimary(EditText value) {
		etNeighbourBigPrimary = value;
	}

	private void setEtNeighbourBigSecondary(EditText value) {
		etNeighbourBigSecondary = value;
	}

	private EditText getEtNeighbourSmallSecondary() {
		return etNeighbourSmallSecondary;
	}

	private EditText getEtNeighbourSmallPrimary() {
		return etNeighbourSmallPrimary;
	}

	private EditText getEtThisTerminal() {
		return etThisTerminal;
	}

	private EditText getEtNeighbourBigPrimary() {
		return etNeighbourBigPrimary;
	}

	private EditText getEtNeighbourBigSecondary() {
		return etNeighbourBigSecondary;
	}

	public void setTerminalNo(int value) {
		terminalNo = value;
	}

	public ConfigInitInfoWindow getCiiWindow() {
		return ciiWindow;
	}
}