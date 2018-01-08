package com.terrytec.brokenrailmonitor;

import java.util.Timer;
import java.util.TimerTask;

import com.terrytec.brokenrailmonitor.Enums.DataLevel;
import com.terrytec.brokenrailmonitor.classes.DensityUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TerminalAnd2Rails extends RelativeLayout {

	private final int ACCESS_POINT_CONNECT_TIMEOUT = 0;
	public int terminalNo;
	public int neighbourSmall;
	public int neighbourBig;
	public boolean is4G;
	public boolean isEnd;

	public boolean isOnline = false;
	public Button btnTerminal;
	public ImageView ivAccessPoint;

	// private Boolean timerIsRunning = false;
	private Timer timer = new Timer();
	private MyTimerTask task;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == ACCESS_POINT_CONNECT_TIMEOUT) {
				((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg(
						("超过2分钟没有收到终端" + String.valueOf(terminalNo) + "的心跳包，它可能已经下线").getBytes(), DataLevel.Error);
				setAccessPointNotConnect();
			}
		}
	};

	public TerminalAnd2Rails(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.terminal_and_2rails, this, true);

		Button btnTerminal = (Button) this.findViewById(R.id.btnTerminal);
		if (btnTerminal != null) {
			btnTerminal.setOnClickListener(btnTerminalListener);
		}
	}

	private OnClickListener btnTerminalListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				HomeFragment homeFragment = (HomeFragment) MainActivity.getMainActivity().homeFragment;
				if (homeFragment.getIsInEditMode()) {
					// Intent是系统各组件之间进行数据传递的数据负载者，即通信使者
					Intent intent = new Intent();

					// 设置页面转向
					intent.setClass(MainActivity.getMainActivity(), EditTerminalActivity.class);
					intent.putExtra("isTerminal", true);
					intent.putExtra("index", String.valueOf(homeFragment.FindMasterControlIndex(terminalNo)));
					// 设置传递参数
					MainActivity.getMainActivity().startActivity(intent);// 不需要接收返回值时使用
				} else {
					if (isOnline) {
						// Intent是系统各组件之间进行数据传递的数据负载者，即通信使者
						Intent intent = new Intent();

						// 设置页面转向
						intent.setClass(MainActivity.getMainActivity(), TerminalCmdActivity.class);
						intent.putExtra("terminalNo", String.valueOf(terminalNo));
						// 设置传递参数
						MainActivity.getMainActivity().startActivity(intent);// 不需要接收返回值时使用
					} else
						Toast.makeText(MainActivity.getMainActivity(), "终端不在线！", Toast.LENGTH_LONG).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	// public TerminalAnd2Rails(Parcel in) {
	// super(MainActivity.getMainActivity().getBaseContext());
	// this.terminalNo = in.readInt();
	// this.neighbourSmall = in.readInt();
	// this.neighbourBig = in.readInt();
	// this.is4G = Boolean.getBoolean(in.readString());
	// this.isEnd = Boolean.getBoolean(in.readString());
	// }
	public void changeLeftRailNormal() {
		Rail leftRail = (Rail) this.findViewById(R.id.railLeft);
		leftRail.setRailNormal();
	}

	public void changeRightRailNormal() {
		Rail rightRail = (Rail) this.findViewById(R.id.railRight);
		rightRail.setRailNormal();
	}

	public void changeLeftRailError() {
		Rail leftRail = (Rail) this.findViewById(R.id.railLeft);
		leftRail.setRailError();
	}

	public void changeRightRailError() {
		Rail rightRail = (Rail) this.findViewById(R.id.railRight);
		rightRail.setRailError();
	}

	public void changeLeftRailTimeout() {
		Rail leftRail = (Rail) this.findViewById(R.id.railLeft);
		leftRail.setRailTimeout();
	}

	public void changeRightRailTimeout() {
		Rail rightRail = (Rail) this.findViewById(R.id.railRight);
		rightRail.setRailTimeout();
	}

	public void changeLeftRailContinuousInterference() {
		Rail leftRail = (Rail) this.findViewById(R.id.railLeft);
		leftRail.setRailContinuousInterference();
	}

	public void changeRightRailContinuousInterference() {
		Rail rightRail = (Rail) this.findViewById(R.id.railRight);
		rightRail.setRailContinuousInterference();
	}

	public void changeLeftRailDifferent() {
		Rail leftRail = (Rail) this.findViewById(R.id.railLeft);
		leftRail.setRailDifferent();
	}

	public void changeRightRailDifferent() {
		Rail rightRail = (Rail) this.findViewById(R.id.railRight);
		rightRail.setRailDifferent();
	}

	public void setRailStressLeft(int value) {
		TextView tvRailStressLeft = (TextView) this.findViewById(R.id.tvRailStressLeft);
		tvRailStressLeft.setText(String.valueOf(value));
	}

	public void setRailStressRight(int value) {
		TextView tvRailStressRight = (TextView) this.findViewById(R.id.tvRailStressRight);
		tvRailStressRight.setText(String.valueOf(value));
	}

	public void setRailTemperatureLeft(int value) {
		TextView tvRailTemperatureLeft = (TextView) this.findViewById(R.id.tvRailTemperatureLeft);
		tvRailTemperatureLeft.setText(String.valueOf(value) + "℃");
	}

	public void setRailTemperatureRight(int value) {
		TextView tvRailTemperatureRight = (TextView) this.findViewById(R.id.tvRailTemperatureRight);
		tvRailTemperatureRight.setText(String.valueOf(value) + "℃");
	}

	public void setMCTemperature(int value) {
		TextView tvMCTemperature = (TextView) this.findViewById(R.id.tvMCTemperature);
		tvMCTemperature.setText(String.valueOf(value) + "℃");
	}

	public void setAllProperty(TerminalAnd2Rails t) {
		setTerminalNo(t.terminalNo);
		setNeighbourSmall(t.neighbourSmall);
		setNeighbourBig(t.neighbourBig);
		setIs4G(t.is4G);
		setIsEnd(t.isEnd);
	}

	public void setAccessPointConnect() {
		isOnline = true;
		if (ivAccessPoint != null) {
			if (timer != null) {
				if (task != null)
					task.cancel(); // 将原任务从队列中移除
				task = new MyTimerTask(); // 新建一个任务
				timer.schedule(task, 125000);
			}
			// else if (timer != null) {
			// timer.schedule(task, 120000);
			// timerIsRunning = true;
			// } else {
			// timer = new Timer();
			// timer.schedule(task, 120000);
			// timerIsRunning = true;
			// }
			ivAccessPoint.setImageResource(R.drawable.connect_normal);
		}
	}

	public void setAccessPointNotConnect() {
		isOnline = false;
		if (ivAccessPoint != null) {
			if (timer != null) {
				if (task != null)
					task.cancel();
				// timerIsRunning = false;
			}
			ivAccessPoint.setImageResource(R.drawable.connect_error);
		}
	}

	public void setTerminalNo(int nember) {
		terminalNo = nember;
		btnTerminal = (Button) this.findViewById(R.id.btnTerminal);
		btnTerminal.setText(String.valueOf(terminalNo));
	}

	public void setNeighbourSmall(int nember) {
		neighbourSmall = nember;
	}

	public void setNeighbourBig(int nember) {
		neighbourBig = nember;
	}

	public void setIs4G(boolean bool) {
		is4G = bool;
		ivAccessPoint = (ImageView) this.findViewById(R.id.ivAccessPoint);
		if (is4G)
			ivAccessPoint.setVisibility(View.VISIBLE);
		else
			ivAccessPoint.setVisibility(View.INVISIBLE);
	}

	public void setIsEnd(boolean bool) {
		isEnd = bool;
	}

	public void setRailsGone() {
		Rail leftRail = (Rail) this.findViewById(R.id.railLeft);
		Rail rightRail = (Rail) this.findViewById(R.id.railRight);

		leftRail.setVisibility(View.GONE);
		rightRail.setVisibility(View.GONE);

		setAddDeleteBtnMargin();
	}

	public void setAddDeleteBtnMargin() {
		Button btnAdd = (Button) this.findViewById(R.id.btnAdd);
		Button btnDelete = (Button) this.findViewById(R.id.btnDelete);

		if (btnAdd != null && btnDelete != null) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnAdd.getLayoutParams();
			layoutParams.setMargins(0, 0, DensityUtil.dip2px(MainActivity.getMainActivity(), 15), 0);
			btnAdd.setLayoutParams(layoutParams);

			LinearLayout.LayoutParams layoutParamsDelete = (LinearLayout.LayoutParams) btnDelete.getLayoutParams();
			layoutParamsDelete.setMargins(DensityUtil.dip2px(MainActivity.getMainActivity(), 15),
					DensityUtil.dip2px(MainActivity.getMainActivity(), 18), 0, 0);
			btnDelete.setLayoutParams(layoutParamsDelete);
		}
	}

	// @Override
	// public int describeContents() {
	// return 0;
	// }
	//
	// public static final Parcelable.Creator<TerminalAnd2Rails> CREATOR = new
	// Parcelable.Creator<TerminalAnd2Rails>() {
	// @Override
	// public TerminalAnd2Rails createFromParcel(Parcel in) {
	// return new TerminalAnd2Rails(in);
	// }
	//
	// @Override
	// public TerminalAnd2Rails[] newArray(int size) {
	// return new TerminalAnd2Rails[size];
	// }
	// };
	//
	// @Override
	// public void writeToParcel(Parcel dest, int flags) {
	// dest.writeInt(this.terminalNo);
	// dest.writeInt(this.neighbourSmall);
	// dest.writeInt(this.neighbourBig);
	// dest.writeString(String.valueOf(this.is4G));
	// dest.writeString(String.valueOf(this.isEnd));
	// }
	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			Message message = new Message();
			message.what = ACCESS_POINT_CONNECT_TIMEOUT;
			handler.sendMessage(message);
		}
	}
}
