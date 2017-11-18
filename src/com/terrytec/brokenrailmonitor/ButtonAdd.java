package com.terrytec.brokenrailmonitor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

public class ButtonAdd extends Button {

	public ButtonAdd(Context context, AttributeSet attrs) {
		super(context, attrs);
		LinearLayout llContainer = (LinearLayout) findViewById(R.id.llTerminalAndRailsContainer);
		LayoutInflater.from(context).inflate(R.layout.button_add, llContainer, false);
//		llContainer.addView(btnAdd);
	}

}
