package com.terrytec.brokenrailmonitor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TerminalAnd2Rails extends RelativeLayout {

	// private static final long serialVersionUID = 1L;
	public int terminalNo;
	public int neighbourSmall;
	public int neighbourBig;
	public boolean is4G;
	public boolean isEnd;

	public Button btnTerminal;
	public ImageView ivAccessPoint;

	public TerminalAnd2Rails(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.terminal_and_2rails, this, true);
	}

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

	public void setAllProperty(TerminalAnd2Rails t) {
		setTerminalNo(t.terminalNo);
		setNeighbourSmall(t.neighbourSmall);
		setNeighbourBig(t.neighbourBig);
		setIs4G(t.is4G);
		setIsEnd(t.isEnd);
	}
	
	public void setAccessPointConnect() {
		if (ivAccessPoint!=null) {
			ivAccessPoint.setImageResource(R.drawable.connect_normal);
		}
	}
	
	public void setAccessPointNotConnect() {
		if (ivAccessPoint!=null) {
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

	// @Override
	// public int describeContents() {
	// // TODO Auto-generated method stub
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
}
