package com.terrytec.brokenrailmonitor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Rail extends RelativeLayout {
	public Rail(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.rail, this, true);
	}

	public void setRailNormal() {
		TextView rail = (TextView) this.findViewById(R.id.tvRail);
		rail.setBackground(getResources().getDrawable(R.drawable.rail_normal,null));
	}
	
	public void setRailError() {
		TextView rail = (TextView) this.findViewById(R.id.tvRail);
		rail.setBackground(getResources().getDrawable(R.drawable.rail_error,null));
	}
}
