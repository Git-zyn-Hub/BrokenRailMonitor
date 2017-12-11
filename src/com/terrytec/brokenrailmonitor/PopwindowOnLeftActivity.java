package com.terrytec.brokenrailmonitor;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.PopupWindow;

public class PopwindowOnLeftActivity extends Activity {
	// ����PopupWindow���������
	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popwindow);
	}

	/**
	 * ����PopupWindow
	 */
	protected void initPopuptWindow() {
		// ��ȡ�Զ��岼���ļ�pop.xml����ͼ
		View popupWindow_view = getLayoutInflater().inflate(R.layout.activity_popwindow, null, false);
		// ����PopupWindowʵ��,200,150�ֱ��ǿ�Ⱥ͸߶�
		popupWindow = new PopupWindow(popupWindow_view, 200, 150, true);
		// ���ö���Ч��
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		// ��������ط���ʧ
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});

		// pop.xml��ͼ����Ŀؼ�
		Button open = (Button) popupWindow_view.findViewById(R.id.open);
		Button save = (Button) popupWindow_view.findViewById(R.id.save);
		Button close = (Button) popupWindow_view.findViewById(R.id.close);
		// pop.xml��ͼ����Ŀؼ��������¼�
		// ��
		open.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �������ִ����ز���
				System.out.println("�򿪲���");
				// �Ի�����ʧ
				popupWindow.dismiss();
			}
		});
		// ����
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �������ִ����ز���
				System.out.println("�������");
				popupWindow.dismiss();
			}
		});
		// �ر�
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �������ִ����ز���
				System.out.println("�رղ���");
				popupWindow.dismiss();
			}
		});
	}
}
