package com.terrytec.brokenrailmonitor;

import java.util.ArrayList;
import java.util.List;

import com.terrytec.brokenrailmonitor.classes.FileOperate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class EditTerminalActivity extends Activity {
	// 声明控件
	// private Button btnButton=null;
	private CustomTitleBar ctbEditTerminal;
	private EditText etNeighbourSmall;
	private EditText etTerminalNo;
	private EditText etNeighbourBig;
	private FileOperate fileOperator;
	private List<TerminalAnd2Rails> terminalAnd2Rails;
	private int index;
	// 此页面按钮btnButton的标志码
	// public final static int result_Code = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 调用并显示页面2
		try {
			setContentView(R.layout.activity_edit_terminal);
			ctbEditTerminal = (CustomTitleBar) findViewById(R.id.ctbEditTerminal);
			ctbEditTerminal.setTitleLeftBtnClickListener(backListener);
			ctbEditTerminal.setTitleRightBtnClickListener(saveListener);

			etNeighbourSmall = (EditText) findViewById(R.id.etNeighbourSmall);
			etTerminalNo = (EditText) findViewById(R.id.etTerminalNo);
			etNeighbourBig = (EditText) findViewById(R.id.etNeighbourBig);
			setKnownInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setKnownInfo() {

		Intent intent = getIntent();
		// 获取该Intent所携带的数据
		Bundle bundle = intent.getExtras();
		// 从bundle数据包中取出数据
		String str = bundle.getString("index");// getString()返回指定key的值
		index = Integer.valueOf(str);
		fileOperator = new FileOperate((MainActivity) MainActivity.getMainActivity());
		terminalAnd2Rails = fileOperator.GetTerminalAnd2Rails();
		if (index == 0 && terminalAnd2Rails.size() > 0) {
			etNeighbourSmall.setText("0");
			etNeighbourBig.setText(String.valueOf(terminalAnd2Rails.get(0).terminalNo));
		} else if (index == 0) {
			etNeighbourSmall.setText("0");
		} else if (index == -2) {
			etNeighbourSmall.setText(String.valueOf(terminalAnd2Rails.get(terminalAnd2Rails.size() - 1).terminalNo));
		} else if (index != -1 && (terminalAnd2Rails.size() > 1)) {
			etNeighbourSmall.setText(String.valueOf(terminalAnd2Rails.get(index - 1).terminalNo));
			etNeighbourBig.setText(String.valueOf(terminalAnd2Rails.get(index).terminalNo));
		}
	}

	// 单击事件监听器
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

	private OnClickListener saveListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			try {
				if (isEmpty(etNeighbourSmall, getResources().getString(R.string.neighbourSmall))
						|| isEmpty(etTerminalNo, getResources().getString(R.string.terminalNo))
						|| isEmpty(etNeighbourBig, getResources().getString(R.string.neighbourBig)))
					return;
				// if (!isInteger(etNeighbourSmall,
				// getResources().getString(R.string.neighbourSmall))
				// || !isInteger(etTerminalNo,
				// getResources().getString(R.string.terminalNo))
				// || !isInteger(etNeighbourBig,
				// getResources().getString(R.string.neighbourBig)))
				// return;
				TerminalAnd2Rails oneTerminal = new TerminalAnd2Rails(MainActivity.getMainActivity(), null);
				oneTerminal.setTerminalNo(getIntFromEditText(etTerminalNo));
				oneTerminal.setNeighbourSmall(getIntFromEditText(etNeighbourSmall));
				oneTerminal.setNeighbourBig(getIntFromEditText(etNeighbourBig));
				// oneTerminal.setIs4G(getBooleanFromEditText());
				RadioButton is4GYes = (RadioButton) findViewById(R.id.rbYes4G);
				RadioButton is4GNo = (RadioButton) findViewById(R.id.rbNo4G);
				RadioButton isEndYes = (RadioButton) findViewById(R.id.rbYesEnd);
				RadioButton isEndNo = (RadioButton) findViewById(R.id.rbNoEnd);
				oneTerminal.setIs4G(getBooleanFromRadioButtons(is4GYes, is4GNo));
				oneTerminal.setIsEnd(getBooleanFromRadioButtons(isEndYes, isEndNo));
				if (terminalAnd2Rails == null) {
					terminalAnd2Rails = new ArrayList<TerminalAnd2Rails>();
				}
				if (index == -1 || index == -2)
					terminalAnd2Rails.add(oneTerminal);
				else
					terminalAnd2Rails.add(index, oneTerminal);

				fileOperator.write(terminalAnd2Rails);

				((HomeFragment) MainActivity.getMainActivity().homeFragment).freshDevices();
				// 完成，关闭当前页面并返回
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	private int getIntFromEditText(EditText et) {
		try {
			return Integer.parseInt(et.getText().toString().trim());
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	private boolean getBooleanFromRadioButtons(RadioButton yes, RadioButton no) {
		try {
			return yes.isChecked() && !no.isChecked();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isInteger(EditText inputET, String etName) {
		try {
			String inputString = inputET.getText().toString().trim();
			Integer.parseInt(inputString);
			return true;
		} catch (NumberFormatException e) {

			Toast.makeText(EditTerminalActivity.this, "请在‘" + etName + "’输入正确的整数", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	private boolean isEmpty(EditText inputET, String etName) {
		String inputString = inputET.getText().toString().trim();
		if (inputString.length() == 0) {
			Toast.makeText(EditTerminalActivity.this, etName + " 不能为空！", Toast.LENGTH_LONG).show();
			return true;
		}
		return false;
	}
}
