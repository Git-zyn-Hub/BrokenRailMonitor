/**
 * FileName:MyFragmentPagerAdapter.java
 * Copyright(C) 2016 zteict
 */
package com.terrytec.brokenrailmonitor;

import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;


/**
 * @author YaoDiWei
 * @version 
 * @see com.imooc.tab03.MyFragmentPagerAdapter.java
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	
	public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}
