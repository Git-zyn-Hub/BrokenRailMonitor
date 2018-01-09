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
	// ����GetDateAndTimeWindow���������
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
	 * ��ȡGetDateAndTimeWindowʵ��
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
		// ��ȡ�Զ��岼���ļ�pop.xml����ͼ
		if (inflaterGlobal == null) {
			return;
		}
		homeFragment = ((HomeFragment) MainActivity.getMainActivity().homeFragment);
		View popupWindow_view = inflaterGlobal.inflate(R.layout.window_get_date_time, null, false);
		// ����PopupWindowʵ��,200,150�ֱ��ǿ�Ⱥ͸߶�
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
		// ���ö���Ч��
		gdTimeWindow.setAnimationStyle(R.style.AnimationFade);
		// ��������ط���ʧ
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

		// pop.xml��ͼ����Ŀؼ�
		final Button btnCancel = (Button) popupWindow_view.findViewById(R.id.btnCancel);

		// pop.xml��ͼ����Ŀؼ��������¼�
		// ��
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// �Ի�����ʧ
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
	 * ���ߣ�
	 * 
	 * īȾ�� ���ӣ�https:// www.jianshu.com/p/d071badafdcf ��Դ�� ����
	 * ����Ȩ���������С���ҵת������ϵ���߻����Ȩ������ҵת����ע��������
	 */

	public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
		// ֱ�Ӵ���һ��DatePickerDialog�Ի���ʵ������������ʾ����
		new DatePickerDialog(activity, themeResId
		// �󶨼�����(How the parent is notified that the date is set.)
				, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// �˴��õ�ѡ���ʱ�䣬���Խ�������Ҫ�Ĳ���
						tv.setText(year + "/" + ++monthOfYear + "/" + dayOfMonth);
					}
				}
				// ���ó�ʼ����
				, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
						.show();
	}

	public static void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
		// Calendar c = Calendar.getInstance();
		// ����һ��TimePickerDialogʵ������������ʾ����
		// ����һ����Activity��context������
		new TimePickerDialog(activity, themeResId,
				// �󶨼�����
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						tv.setText(hourOfDay + ":" + minute);
					}
				}
				// ���ó�ʼʱ��
				, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)
				// true��ʾ����24Сʱ��
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
