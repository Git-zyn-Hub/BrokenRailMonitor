package com.terrytec.brokenrailmonitor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class Terminal extends RelativeLayout {

	public Terminal(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.terminal, this, true);
	}
}
