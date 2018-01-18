package com.terrytec.brokenrailmonitor;

import java.util.ArrayList;
import java.util.List;

import com.terrytec.brokenrailmonitor.datafragments.BaseFragment;
import com.terrytec.brokenrailmonitor.datafragments.Rail1ThisAmplitudeFragment;
import com.terrytec.brokenrailmonitor.datafragments.Rail2ThisAmplitudeFragment;
import com.terrytec.brokenrailmonitor.datafragments.SignalAmplitudeFragment;
import com.terrytec.brokenrailmonitor.datafragments.StressFragment;
import com.terrytec.brokenrailmonitor.datafragments.TemperatureFragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class HistoryFragment extends Fragment implements OnClickListener {
	private View vTabData;
	private TextView tvTemperature, tvRail1ThisAmplitude, tvRail2ThisAmplitude, tvRail1Stress, tvRail2Stress,
			tvRail1LeftSignalAmplitude, tvRail1RightSignalAmplitude, tvRail2LeftSignalAmplitude,
			tvRail2RightSignalAmplitude, tvCurrent;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private MyViewPager viewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("zyn", this.getClass().getName() + " onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("zyn", this.getClass().getName() + " onCreateView");
		vTabData = inflater.inflate(R.layout.tabhistory, container, false);
		try {
			initView();
			initData();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return vTabData;
	}

	private void initData() {
		// LayoutInflater mInflater = LayoutInflater.from(this);
		//
		// View tabhome = mInflater.inflate(R.layout.tabhome, null);
		// View tabcommand = mInflater.inflate(R.layout.tabcommand, null);
		// View tabhistory = mInflater.inflate(R.layout.tabhistory, null);
		// views.add(tabhome);
		// views.add(tabcommand);
		// views.add(tabhistory);
		//
		// MyPagerAdapter adapter = new MyPagerAdapter(views);
		// viewPager.setAdapter(adapter);

		BaseFragment temperatureFragment = new TemperatureFragment(R.layout.fragment_chart);
		BaseFragment rail1ThisAmplitudeFragment = new Rail1ThisAmplitudeFragment(R.layout.fragment_chart);
		BaseFragment rail2ThisAmplitudeFragment = new Rail2ThisAmplitudeFragment(R.layout.fragment_chart);
		BaseFragment rail1StressFragment = new StressFragment(R.layout.fragment_chart);
		BaseFragment rail2StressFragment = new StressFragment(R.layout.fragment_chart);
		BaseFragment rail1LeftSignalAmplitudeFragment = new SignalAmplitudeFragment(R.layout.fragment_chart);
		BaseFragment rail1RightSignalAmplitudeFragment = new SignalAmplitudeFragment(R.layout.fragment_chart);
		BaseFragment rail2LeftSignalAmplitudeFragment = new SignalAmplitudeFragment(R.layout.fragment_chart);
		BaseFragment rail2RightSignalAmplitudeFragment = new SignalAmplitudeFragment(R.layout.fragment_chart);

		temperatureFragment.setTitle(getResources().getString(R.string.Temperature));
		rail1ThisAmplitudeFragment.setTitle(getResources().getString(R.string.strRail1ThisAmplitude));
		rail2ThisAmplitudeFragment.setTitle(getResources().getString(R.string.strRail2ThisAmplitude));
		rail1StressFragment.setTitle(getResources().getString(R.string.strRail1Stress));
		rail2StressFragment.setTitle(getResources().getString(R.string.strRail2Stress));
		rail1LeftSignalAmplitudeFragment.setTitle(getResources().getString(R.string.strRail1LeftSignalAmplitude));
		rail1RightSignalAmplitudeFragment.setTitle(getResources().getString(R.string.strRail1RightSignalAmplitude));
		rail2LeftSignalAmplitudeFragment.setTitle(getResources().getString(R.string.strRail2LeftSignalAmplitude));
		rail2RightSignalAmplitudeFragment.setTitle(getResources().getString(R.string.strRail2RightSignalAmplitude));
		
		fragments.add(temperatureFragment);
		fragments.add(rail1ThisAmplitudeFragment);
		fragments.add(rail2ThisAmplitudeFragment);
		fragments.add(rail1StressFragment);
		fragments.add(rail2StressFragment);
		fragments.add(rail1LeftSignalAmplitudeFragment);
		fragments.add(rail1RightSignalAmplitudeFragment);
		fragments.add(rail2LeftSignalAmplitudeFragment);
		fragments.add(rail2RightSignalAmplitudeFragment);

		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager(), fragments);
		viewPager.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		changeTab(v.getId());
	}

	private void initView() {
		viewPager = (MyViewPager) vTabData.findViewById(R.id.viewPagerData);

		tvTemperature = (TextView) vTabData.findViewById(R.id.tvTemperature);
		tvRail1ThisAmplitude = (TextView) vTabData.findViewById(R.id.tvRail1ThisAmplitude);
		tvRail2ThisAmplitude = (TextView) vTabData.findViewById(R.id.tvRail2ThisAmplitude);
		tvRail1Stress = (TextView) vTabData.findViewById(R.id.tvRail1Stress);
		tvRail2Stress = (TextView) vTabData.findViewById(R.id.tvRail2Stress);
		tvRail1LeftSignalAmplitude = (TextView) vTabData.findViewById(R.id.tvRail1LeftSignalAmplitude);
		tvRail1RightSignalAmplitude = (TextView) vTabData.findViewById(R.id.tvRail1RightSignalAmplitude);
		tvRail2LeftSignalAmplitude = (TextView) vTabData.findViewById(R.id.tvRail2LeftSignalAmplitude);
		tvRail2RightSignalAmplitude = (TextView) vTabData.findViewById(R.id.tvRail2RightSignalAmplitude);

		tvTemperature.setOnClickListener(this);
		tvRail1ThisAmplitude.setOnClickListener(this);
		tvRail2ThisAmplitude.setOnClickListener(this);
		tvRail1Stress.setOnClickListener(this);
		tvRail2Stress.setOnClickListener(this);
		tvRail1LeftSignalAmplitude.setOnClickListener(this);
		tvRail1RightSignalAmplitude.setOnClickListener(this);
		tvRail2LeftSignalAmplitude.setOnClickListener(this);
		tvRail2RightSignalAmplitude.setOnClickListener(this);

		tvTemperature.setSelected(true);

		tvCurrent = tvTemperature;

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				changeTab(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

		});
		viewPager.setOffscreenPageLimit(9);
	}

	private void changeTab(int id) {
		tvCurrent.setSelected(false);
		switch (id) {
		case R.id.tvTemperature:
			viewPager.setCurrentItem(0);
		case 0:
			tvTemperature.setSelected(true);
			tvCurrent = tvTemperature;
			break;
		case R.id.tvRail1ThisAmplitude:
			viewPager.setCurrentItem(1);
		case 1:
			tvRail1ThisAmplitude.setSelected(true);
			tvCurrent = tvRail1ThisAmplitude;
			break;
		case R.id.tvRail2ThisAmplitude:
			viewPager.setCurrentItem(2);
		case 2:
			tvRail2ThisAmplitude.setSelected(true);
			tvCurrent = tvRail2ThisAmplitude;
			break;
		case R.id.tvRail1Stress:
			viewPager.setCurrentItem(3);
		case 3:
			tvRail1Stress.setSelected(true);
			tvCurrent = tvRail1Stress;
			break;
		case R.id.tvRail2Stress:
			viewPager.setCurrentItem(4);
		case 4:
			tvRail2Stress.setSelected(true);
			tvCurrent = tvRail2Stress;
			break;
		case R.id.tvRail1LeftSignalAmplitude:
			viewPager.setCurrentItem(5);
		case 5:
			tvRail1LeftSignalAmplitude.setSelected(true);
			tvCurrent = tvRail1LeftSignalAmplitude;
			break;
		case R.id.tvRail1RightSignalAmplitude:
			viewPager.setCurrentItem(6);
		case 6:
			tvRail1RightSignalAmplitude.setSelected(true);
			tvCurrent = tvRail1RightSignalAmplitude;
			break;
		case R.id.tvRail2LeftSignalAmplitude:
			viewPager.setCurrentItem(7);
		case 7:
			tvRail2LeftSignalAmplitude.setSelected(true);
			tvCurrent = tvRail2LeftSignalAmplitude;
			break;
		case R.id.tvRail2RightSignalAmplitude:
			viewPager.setCurrentItem(8);
		case 8:
			tvRail2RightSignalAmplitude.setSelected(true);
			tvCurrent = tvRail2RightSignalAmplitude;
			break;
		default:
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e("zyn", this.getClass().getName() + " onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e("zyn", this.getClass().getName() + " onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("zyn", this.getClass().getName() + " onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e("zyn", this.getClass().getName() + " onDestroyView");
	}
}
