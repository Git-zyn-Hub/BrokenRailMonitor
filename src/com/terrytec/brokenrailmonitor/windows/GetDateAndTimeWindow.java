package com.terrytec.brokenrailmonitor.windows;

import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.Text;

import com.terrytec.brokenrailmonitor.HomeFragment;
import com.terrytec.brokenrailmonitor.MainActivity;
import com.terrytec.brokenrailmonitor.R;
import com.terrytec.brokenrailmonitor.Enums.CommandType;
import com.terrytec.brokenrailmonitor.classes.SendDataPackage;

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
import android.widget.Toast;

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
	private Calendar dateTimeStart = Calendar.getInstance();
	private Calendar dateTimeEnd = Calendar.getInstance();
	private int terminalNo;

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

		gdTimeWindow.getTvDateStart().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(activity, 2, gdTimeWindow.getTvDateStart(), dateTimeStart);
			}
		});
		gdTimeWindow.getTvTimeStart().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePickerDialog(activity, 2, gdTimeWindow.getTvTimeStart(), dateTimeStart);
			}
		});
		gdTimeWindow.getTvDateEnd().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(activity, 2, gdTimeWindow.getTvDateEnd(), dateTimeEnd);
			}
		});
		gdTimeWindow.getTvTimeEnd().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePickerDialog(activity, 2, gdTimeWindow.getTvTimeEnd(), dateTimeEnd);
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
				if (!homeFragment.getIsConnect()) {
					Toast.makeText(MainActivity.getMainActivity(), "�������ӣ�", Toast.LENGTH_LONG).show();
					return;
				}
				homeFragment.sendBytesBuffer = SendDataPackage.PackageSendData(
						(byte) MainActivity.getMainActivity().ClientID, (byte) terminalNo,
						(byte) CommandType.GetHistory.getValue(),
						new byte[] { (byte) (dateTimeStart.get(Calendar.YEAR) % 100),
								(byte) (dateTimeStart.get(Calendar.MONTH) + 1),
								(byte) dateTimeStart.get(Calendar.DAY_OF_MONTH),
								(byte) dateTimeStart.get(Calendar.HOUR_OF_DAY),
								(byte) dateTimeStart.get(Calendar.MINUTE), (byte) 0,
								(byte) (dateTimeEnd.get(Calendar.YEAR) % 100),
								(byte) (dateTimeEnd.get(Calendar.MONTH) + 1),
								(byte) dateTimeEnd.get(Calendar.DAY_OF_MONTH),
								(byte) dateTimeEnd.get(Calendar.HOUR_OF_DAY), (byte) dateTimeEnd.get(Calendar.MINUTE),
								(byte) 0 });
				new Thread(homeFragment.sendBytesThread).start();
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

	public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv,
			final Calendar calendar) {
		// ֱ�Ӵ���һ��DatePickerDialog�Ի���ʵ������������ʾ����
		new DatePickerDialog(activity, themeResId
		// �󶨼�����(How the parent is notified that the date is set.)
				, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// �˴��õ�ѡ���ʱ�䣬���Խ�������Ҫ�Ĳ���
						calendar.set(year, monthOfYear, dayOfMonth);
						tv.setText(year + "/" + ++monthOfYear + "/" + dayOfMonth);
					}
				}
				// ���ó�ʼ����
				, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
						.show();
	}

	public static void showTimePickerDialog(Activity activity, int themeResId, final TextView tv,
			final Calendar calendar) {
		// Calendar c = Calendar.getInstance();
		// ����һ��TimePickerDialogʵ������������ʾ����
		// ����һ����Activity��context������
		new TimePickerDialog(activity, themeResId,
				// �󶨼�����
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
								calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
						tv.setText(String.format("%2d", hourOfDay).replace(" ", "0") + ":"
								+ String.format("%2d", minute).replace(" ", "0"));
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

	public void setTerminalNo(int value) {
		terminalNo = value;
	}

}
