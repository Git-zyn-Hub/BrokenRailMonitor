package com.terrytec.brokenrailmonitor;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	// private List<View> views = new ArrayList<View>();

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private MyViewPager viewPager;
	private LinearLayout llHome, llCommand, llHistory;
	private ImageView ivHome, ivCommand, ivHistory, ivCurrent;
	private TextView tvHome, tvCommand, tvHistory, tvCurrent;
	private CustomTitleBar ctbHomeTitle;
	// private Button btnTest;
	// 本页面传递时的按钮btn的标志码
	private final static int request_Code = 11;
	private static MainActivity mainActivity;
	public Fragment homeFragment;
	public Fragment commandFragment;
	public int ClientID=0;
	
	public MainActivity() {
	    mainActivity = this;
	}

	public static MainActivity getMainActivity() {
	    return mainActivity;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		initData();

		// LayoutInflater inflater = LayoutInflater.from(this);
		// View view = inflater.inflate(R.layout.tabhome, null);
		//
		// btnEdit=(Button)view.findViewById(R.id.btnEdit);
		// btnEdit.setOnClickListener(btnEditListener);
	}

	// private OnClickListener btnEditListener=new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// //Intent是系统各组件之间进行数据传递的数据负载者，即通信使者
	// Intent intent=new Intent();
	//
	// //设置页面转向
	// intent.setClass(MainActivity.this,EditTerminalActivity.class);
	// //设置传递参数
	// intent.putExtra("str", "你好");
	// //startActivity(intent);不需要接收返回值时使用
	// //接收返回值，request_Code代表当前按钮btn的请求吗
	// startActivityForResult(intent, request_Code);
	// }
	// };

	private OnClickListener btnEditListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// Intent是系统各组件之间进行数据传递的数据负载者，即通信使者
			Intent intent = new Intent();

			// 设置页面转向
			intent.setClass(MainActivity.this, EditTerminalActivity.class);
			// 设置传递参数
			intent.putExtra("str", "你好");
			// startActivity(intent);不需要接收返回值时使用
			// 接收返回值，request_Code代表当前按钮btn的请求吗
			startActivityForResult(intent, request_Code);
		}
	};

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

		homeFragment = new HomeFragment();
		commandFragment = new CommandFragment();
		Fragment historyFragment = new HistoryFragment();

		fragments.add(homeFragment);
		fragments.add(commandFragment);
		fragments.add(historyFragment);

		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager(), fragments);
		viewPager.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		changeTab(v.getId());
		// if (v.getId() == R.id.btnEdit) {
		// // Intent是系统各组件之间进行数据传递的数据负载者，即通信使者
		// Intent intent = new Intent();
		//
		// // 设置页面转向
		// intent.setClass(MainActivity.this, EditTerminalActivity.class);
		// // 设置传递参数
		// intent.putExtra("str", "你好");
		// // startActivity(intent);不需要接收返回值时使用
		// // 接收返回值，request_Code代表当前按钮btn的请求吗
		// startActivityForResult(intent, request_Code);
		// }
	}

	private void initView() {
		viewPager = (MyViewPager) findViewById(R.id.viewPager);
		// LayoutInflater.from(this).inflate(R.id.ctbHomeTitle, viewPager,
		// true);

		// llMainActivity = (LinearLayout) findViewById(R.id.llMainActivity);
		llHome = (LinearLayout) findViewById(R.id.llHome);
		llCommand = (LinearLayout) findViewById(R.id.llCommand);
		llHistory = (LinearLayout) findViewById(R.id.llHistory);

		llHome.setOnClickListener(this);
		llCommand.setOnClickListener(this);
		llHistory.setOnClickListener(this);

		ivHome = (ImageView) findViewById(R.id.ivHome);
		ivCommand = (ImageView) findViewById(R.id.ivCommand);
		ivHistory = (ImageView) findViewById(R.id.ivHistory);

		tvHome = (TextView) findViewById(R.id.tvHome);
		tvCommand = (TextView) findViewById(R.id.tvCommand);
		tvHistory = (TextView) findViewById(R.id.tvHistory);

		// llMainActivity.getView
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.tabhome, null);
		ctbHomeTitle = (CustomTitleBar) view.findViewById(R.id.ctbHomeTitle);
		ctbHomeTitle.setTitleRightBtnClickListener(btnEditListener);

		ivHome.setSelected(true);
		tvHome.setSelected(true);

		ivCurrent = ivHome;
		tvCurrent = tvHome;

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
		viewPager.setOffscreenPageLimit(2);
	}

	private void changeTab(int id) {
		ivCurrent.setSelected(false);
		tvCurrent.setSelected(false);
		switch (id) {
		case R.id.llHome:
			viewPager.setCurrentItem(0);
		case 0:
			ivHome.setSelected(true);
			ivCurrent = ivHome;
			tvHome.setSelected(true);
			tvCurrent = tvHome;
			break;
		case R.id.llCommand:
			viewPager.setCurrentItem(1);
		case 1:
			ivCommand.setSelected(true);
			ivCurrent = ivCommand;
			tvCommand.setSelected(true);
			tvCurrent = tvCommand;
			break;
		case R.id.llHistory:
			viewPager.setCurrentItem(2);
		case 2:
			ivHistory.setSelected(true);
			ivCurrent = ivHistory;
			tvHistory.setSelected(true);
			tvCurrent = tvHistory;
			break;
		default:
			break;
		}
	}
}
