package com.terrytec.brokenrailmonitor.windows;

import com.terrytec.brokenrailmonitor.R;

import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PointConfigInfoWindow extends PopupWindow {

	private LayoutInflater inflaterGlobal;
	// 声明PointConfigInfoWindow对象的引用
	private PointConfigInfoWindow pConfigInfoWindow;

	private int terminalNo;
	private int neighbourSmallSecondary;
	private int neighbourSmall;
	private int neighbourBig;
	private int neighbourBigSecondary;
	private boolean flashIsValid;

	private TextView tvTerminalNoOfConfig;
	private TextView tvNeighbourSmallSecondary;
	private TextView tvNeighbourSmallPrimary;
	private TextView tvNeighbourBigPrimary;
	private TextView tvNeighbourBigSecondary;
	private TextView tvFlashValidOrNot;

	public PointConfigInfoWindow() {
	}

	public PointConfigInfoWindow(int terminalNo, int neighbourSmallSecondary, int neighbourSmall, int neighbourBig,
			int neighbourBigSecondary, boolean flashIsValid) {

		this.terminalNo = terminalNo;
		this.neighbourSmallSecondary = neighbourSmallSecondary;
		this.neighbourSmall = neighbourSmall;
		this.neighbourBig = neighbourBig;
		this.neighbourBigSecondary = neighbourBigSecondary;
		this.flashIsValid = flashIsValid;
	}

	public PointConfigInfoWindow(View contentView, int width, int height, boolean focusable) {
		super(contentView, width, height, focusable);
	}

	public void setLayoutInflater(LayoutInflater inflater) {
		inflaterGlobal = inflater;
	}

	/***
	 * 获取GetDateAndTimeWindow实例
	 */
	public PointConfigInfoWindow getPointConfigInfoWindow() {
		if (null != pConfigInfoWindow) {
			return pConfigInfoWindow;
		} else {
			initPointConfigInfoWindow();
			return pConfigInfoWindow;
		}
	}

	protected void initPointConfigInfoWindow() {
		// 获取自定义布局文件pop.xml的视图
		if (inflaterGlobal == null) {
			return;
		}
		View popupWindow_view = inflaterGlobal.inflate(R.layout.window_point_config_info, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		pConfigInfoWindow = new PointConfigInfoWindow(popupWindow_view, 700, 800, true);

		tvTerminalNoOfConfig = (TextView) popupWindow_view.findViewById(R.id.tvTerminalNoOfConfig);
		tvNeighbourSmallSecondary = (TextView) popupWindow_view.findViewById(R.id.tvNeighbourSmallSecondary);
		tvNeighbourSmallPrimary = (TextView) popupWindow_view.findViewById(R.id.tvNeighbourSmallPrimary);
		tvNeighbourBigPrimary = (TextView) popupWindow_view.findViewById(R.id.tvNeighbourBigPrimary);
		tvNeighbourBigSecondary = (TextView) popupWindow_view.findViewById(R.id.tvNeighbourBigSecondary);
		tvFlashValidOrNot = (TextView) popupWindow_view.findViewById(R.id.tvFlashValidOrNot);

		tvTerminalNoOfConfig.setText(terminalNo + "号终端");
		tvNeighbourSmallSecondary.setText(String.valueOf(neighbourSmallSecondary));
		tvNeighbourSmallPrimary.setText(String.valueOf(neighbourSmall));
		tvNeighbourBigPrimary.setText(String.valueOf(neighbourBig));
		tvNeighbourBigSecondary.setText(String.valueOf(neighbourBigSecondary));
		tvFlashValidOrNot.setText(flashIsValid ? "有效" : "无效");
		
		TextPaint paint = tvTerminalNoOfConfig.getPaint(); 
		paint.setFakeBoldText(true);
		if (flashIsValid) {
			tvFlashValidOrNot.setBackgroundResource(R.color.lightgreen);  
		}else
			tvFlashValidOrNot.setBackgroundResource(R.color.red);

		setTvTerminalNoOfConfig(tvTerminalNoOfConfig);
		setTvNeighbourSmallSecondary(tvNeighbourSmallSecondary);
		setTvNeighbourSmallPrimary(tvNeighbourSmallPrimary);
		setTvNeighbourBigPrimary(tvNeighbourBigPrimary);
		setTvNeighbourBigSecondary(tvNeighbourBigSecondary);
		setTvFlashValidOrNot(tvFlashValidOrNot);
		
		// popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.layout_popupwindow_style01,
		// null));
		// 设置动画效果
		pConfigInfoWindow.setAnimationStyle(R.style.AnimationFade);
		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (pConfigInfoWindow != null && pConfigInfoWindow.isShowing()) {
					pConfigInfoWindow.dismiss();
					pConfigInfoWindow = null;
				}
				return false;
			}
		});

		// pop.xml视图里面的控件

		final Button btnOK = (Button) popupWindow_view.findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// 对话框消失
					pConfigInfoWindow.dismiss();
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		});
	}

	private void setTvTerminalNoOfConfig(TextView tView) {
		tvTerminalNoOfConfig = tView;
	}

	private void setTvNeighbourSmallSecondary(TextView tView) {
		tvNeighbourSmallSecondary = tView;
	}

	private void setTvNeighbourSmallPrimary(TextView tView) {
		tvNeighbourSmallPrimary = tView;
	}

	private void setTvNeighbourBigPrimary(TextView tView) {
		tvNeighbourBigPrimary = tView;
	}

	private void setTvNeighbourBigSecondary(TextView tView) {
		tvNeighbourBigSecondary = tView;
	}

	private void setTvFlashValidOrNot(TextView tView) {
		tvFlashValidOrNot = tView;
	}
}
