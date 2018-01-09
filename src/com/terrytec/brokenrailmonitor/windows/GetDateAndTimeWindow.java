package com.terrytec.brokenrailmonitor.windows;

import java.util.Calendar;

import org.w3c.dom.Text;

import com.terrytec.brokenrailmonitor.HomeFragment;
import com.terrytec.brokenrailmonitor.MainActivity;
import com.terrytec.brokenrailmonitor.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

public class GetDateAndTimeWindow extends PopupWindow {

	private LayoutInflater inflaterGlobal;
	// 声明GetDateAndTimeWindow对象的引用
	private GetDateAndTimeWindow gdTimeWindow;
	private HomeFragment homeFragment;
	private TextView tvDateStart;
	private TextView tvTimeStart;
	private TextView tvDateEnd;
	private TextView tvTimeEnd;
	private Activity activity;

	public GetDateAndTimeWindow() {
	}

	public GetDateAndTimeWindow(View contentView, int width, int height, boolean focusable) {
		super(contentView, width, height, focusable);
	}

	public void setLayoutInflater(LayoutInflater inflater) {
		inflaterGlobal = inflater;
	}

	/***
	 * 获取GetDateAndTimeWindow实例
	 */
	public GetDateAndTimeWindow getGetDateAndTimeWindow() {
		if (null != gdTimeWindow) {
			return gdTimeWindow;
		} else {
			initGetDateAndTimeWindow();
			return gdTimeWindow;
		}
	}

	protected void initGetDateAndTimeWindow() {
		// 获取自定义布局文件pop.xml的视图
		if (inflaterGlobal == null) {
			return;
		}
		homeFragment = ((HomeFragment) MainActivity.getMainActivity().homeFragment);
		View popupWindow_view = inflaterGlobal.inflate(R.layout.window_get_date_time, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		gdTimeWindow = new GetDateAndTimeWindow(popupWindow_view, 700, 800, true);
		tvDateStart = (TextView) popupWindow_view.findViewById(R.id.tvDateStart);
		tvTimeStart = (TextView) popupWindow_view.findViewById(R.id.tvTimeStart);
		tvDateEnd = (TextView) popupWindow_view.findViewById(R.id.tvDateEnd);
		tvTimeEnd = (TextView) popupWindow_view.findViewById(R.id.tvTimeEnd);
		gdTimeWindow.setTvDateStart(tvDateStart);
		gdTimeWindow.setTvTimeStart(tvTimeStart);
		gdTimeWindow.setTvDateEnd(tvDateEnd);
		gdTimeWindow.setTvTimeEnd(tvTimeEnd);
		final Calendar calendar = Calendar.getInstance();
		gdTimeWindow.getTvDateStart().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(activity, 2, gdTimeWindow.getTvDateStart(), calendar);
			}
		});
		gdTimeWindow.getTvTimeStart().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTimePickerDialog(activity, 2, gdTimeWindow.getTvTimeStart(), calendar);
			}
		});
		gdTimeWindow.getTvDateEnd().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDatePickerDialog(activity, 2, gdTimeWindow.getTvDateEnd(), calendar);
			}
		});
		gdTimeWindow.getTvTimeEnd().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTimePickerDialog(activity, 2, gdTimeWindow.getTvTimeEnd(), calendar);
			}
		});
		// popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.layout_popupwindow_style01,
		// null));
		// 设置动画效果
		gdTimeWindow.setAnimationStyle(R.style.AnimationFade);
		// 点击其他地方消失
		// popupWindow_view.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (pwdWindow != null && pwdWindow.isShowing()) {
		// pwdWindow.dismiss();
		// pwdWindow = null;
		// }
		// return false;
		// }
		// });

		// pop.xml视图里面的控件
		final Button btnCancel = (Button) popupWindow_view.findViewById(R.id.btnCancel);

		// pop.xml视图里面的控件触发的事件
		// 打开
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// 对话框消失
					gdTimeWindow.dismiss();
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		});

		final Button btnOK = (Button) popupWindow_view.findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	/*
	 * 
	 * 作者：
	 * 
	 * 墨染书 链接：https:// www.jianshu.com/p/d071badafdcf 來源： 简书
	 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
	 */

	public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
		// 直接创建一个DatePickerDialog对话框实例，并将它显示出来
		new DatePickerDialog(activity, themeResId
		// 绑定监听器(How the parent is notified that the date is set.)
				, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// 此处得到选择的时间，可以进行你想要的操作
						tv.setText(year + "/" + ++monthOfYear + "/" + dayOfMonth);
					}
				}
				// 设置初始日期
				, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
						.show();
	}

	public static void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
		// Calendar c = Calendar.getInstance();
		// 创建一个TimePickerDialog实例，并把它显示出来
		// 解释一哈，Activity是context的子类
		new TimePickerDialog(activity, themeResId,
				// 绑定监听器
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						tv.setText(hourOfDay + ":" + minute);
					}
				}
				// 设置初始时间
				, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)
				// true表示采用24小时制
				, true).show();
	}

	private void setTvDateStart(TextView tView) {
		tvDateStart = tView;
	}

	private void setTvTimeStart(TextView tView) {
		tvTimeStart = tView;
	}

	private void setTvDateEnd(TextView tView) {
		tvDateEnd = tView;
	}

	private void setTvTimeEnd(TextView tView) {
		tvTimeEnd = tView;
	}

	private TextView getTvDateStart() {
		return tvDateStart;
	}

	private TextView getTvTimeStart() {
		return tvTimeStart;
	}

	private TextView getTvDateEnd() {
		return tvDateEnd;
	}

	private TextView getTvTimeEnd() {
		return tvTimeEnd;
	}

	public void setActivity(Activity ac) {
		activity = ac;
	}
}
