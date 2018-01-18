package com.terrytec.brokenrailmonitor.datafragments;

import com.terrytec.brokenrailmonitor.R;

import android.R.string;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseFragment extends Fragment {

	private View viewFragment;
	private TextView tvTitle;
	private int resourceID;
	private String title;

	public BaseFragment(int resID) {
		resourceID = resID;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("zyn", this.getClass().getName() + " onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewFragment = inflater.inflate(resourceID, container, false);
		tvTitle = (TextView) viewFragment.findViewById(R.id.tvTitle);
		tvTitle.setText(title);
		return viewFragment;
	}

	public void setTitle(String s) {
		try {
			title = s;
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
}
