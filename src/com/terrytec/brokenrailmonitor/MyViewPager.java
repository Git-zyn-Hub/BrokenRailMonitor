package com.terrytec.brokenrailmonitor;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {
	private int preX = 0;
	private boolean noScroll = false;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setNoScroll(boolean noScroll) {
		this.noScroll = noScroll;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent even) {

		if (noScroll)
			return false;
		else {
			if (even.getAction() == MotionEvent.ACTION_DOWN) {
				preX = (int) even.getX();
			} else {
				if (Math.abs((int) even.getX() - preX) > 300) {
					return true;
				} else {
					preX = (int) even.getX();
				}
			}
			return super.onInterceptTouchEvent(even);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		/* return false;//super.onTouchEvent(arg0); */
		if (noScroll)
			return false;
		else
			return super.onTouchEvent(arg0);
	}
}
