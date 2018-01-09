package com.terrytec.brokenrailmonitor.windows;

import com.terrytec.brokenrailmonitor.Enums.CommandType;
import com.terrytec.brokenrailmonitor.HomeFragment;
import com.terrytec.brokenrailmonitor.MainActivity;
import com.terrytec.brokenrailmonitor.R;
import com.terrytec.brokenrailmonitor.classes.SendDataPackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ConfigInitInfoWindow extends PopupWindow {

	private LayoutInflater inflaterGlobal;
	// 声明PopupWindow对象的引用
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
	 * 获取ConfigInitInfoWindow实例
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
	 * 创建PopupWindow
	 */
	protected void initConfigInitInfoWindow() {
		// 获取自定义布局文件pop.xml的视图
		if (inflaterGlobal == null) {
			return;
		}
		homeFragment = ((HomeFragment) MainActivity.getMainActivity().homeFragment);
		View popupWindow_view = inflaterGlobal.inflate(R.layout.window_config_init_info, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
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
		// 设置动画效果
		ciiWindow.setAnimationStyle(R.style.AnimationFade);
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
				if (isEmpty(ciiWindow.getEtNeighbourSmallSecondary(),
						homeFragment.getResources().getString(R.string.NeighbourSmallSecondary))
						|| isEmpty(ciiWindow.getEtNeighbourSmallPrimary(),
								homeFragment.getResources().getString(R.string.neighbourSmall))
						|| isEmpty(ciiWindow.getEtThisTerminal(),
								homeFragment.getResources().getString(R.string.ThisTerminal))
						|| isEmpty(ciiWindow.getEtNeighbourBigPrimary(),
								homeFragment.getResources().getString(R.string.neighbourBig))
						|| isEmpty(ciiWindow.getEtNeighbourBigSecondary(),
								homeFragment.getResources().getString(R.string.NeighbourBigSecondary)))
					return;
				IntegerClass intNbSmallSecond = new IntegerClass();
				IntegerClass intNbSmallPrimary = new IntegerClass();
				IntegerClass intThisTerminal = new IntegerClass();
				IntegerClass intNbBigPrimary = new IntegerClass();
				IntegerClass intNbBigSecond = new IntegerClass();
				if (isOutOfRange(ciiWindow.getEtNeighbourSmallSecondary(),
						homeFragment.getResources().getString(R.string.NeighbourSmallSecondary), intNbSmallSecond)
						|| isOutOfRange(ciiWindow.getEtNeighbourSmallPrimary(),
								homeFragment.getResources().getString(R.string.neighbourSmall), intNbSmallPrimary)
						|| isOutOfRange(ciiWindow.getEtThisTerminal(),
								homeFragment.getResources().getString(R.string.ThisTerminal), intThisTerminal)
						|| isOutOfRange(ciiWindow.getEtNeighbourBigPrimary(),
								homeFragment.getResources().getString(R.string.neighbourBig), intNbBigPrimary)
						|| isOutOfRange(ciiWindow.getEtNeighbourBigSecondary(),
								homeFragment.getResources().getString(R.string.NeighbourBigSecondary), intNbBigSecond))
					return;
				homeFragment.sendBytesBuffer = SendDataPackage.PackageSendData(
						(byte) MainActivity.getMainActivity().ClientID, (byte) terminalNo,
						(byte) CommandType.ConfigInitialInfo.getValue(),
						new byte[] { Byte.valueOf(intThisTerminal.integer.toString()),
								Byte.valueOf(intNbSmallSecond.integer.toString()),
								Byte.valueOf(intNbSmallPrimary.integer.toString()),
								Byte.valueOf(intNbBigPrimary.integer.toString()),
								Byte.valueOf(intNbBigSecond.integer.toString()), (byte) 0x00 });
				new Thread(homeFragment.sendBytesThread).start();
			}
		});
	}

	private void setInitialInfo() {
		if (homeFragment != null) {
			// 初始化默认的终端号。
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

	private boolean isOutOfRange(EditText inputET, String etName, IntegerClass ic) {
		try {
			String inputString = inputET.getText().toString().trim();
			int inputInt = Integer.parseInt(inputString);
			ic.integer = inputInt;
			if (inputInt < 0 || inputInt > 255) {
				Toast.makeText(MainActivity.getMainActivity(), "请在‘" + etName + "’输入0到255之间的整数", Toast.LENGTH_LONG)
						.show();
				return true;
			} else
				return false;
		} catch (NumberFormatException e) {
			Toast.makeText(MainActivity.getMainActivity(), "请在‘" + etName + "’输入正确的整数", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	private boolean isEmpty(EditText inputET, String etName) {
		String inputString = inputET.getText().toString().trim();
		if (inputString.length() == 0) {
			Toast.makeText(MainActivity.getMainActivity(), etName + " 不能为空！", Toast.LENGTH_LONG).show();
			return true;
		}
		return false;
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

class IntegerClass {
	Integer integer;
}
