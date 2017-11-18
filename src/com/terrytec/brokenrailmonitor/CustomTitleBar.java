package com.terrytec.brokenrailmonitor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

	public class CustomTitleBar extends RelativeLayout {
	    private Button titleBarLeftBtn;
	    private Button titleBarRightBtn;
	    private TextView titleBarTitle;

	    public CustomTitleBar(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        LayoutInflater.from(context).inflate(R.layout.title_bar, this, true);
	        titleBarLeftBtn = (Button) findViewById(R.id.title_bar_left);
	        titleBarRightBtn = (Button) findViewById(R.id.title_bar_right);
	        titleBarTitle = (TextView) findViewById(R.id.title_bar_title);

	        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar);
	        if (attributes != null) {
	            //����titleBar����ɫ
	            int titleBarBackGround = attributes.getResourceId(R.styleable.CustomTitleBar_title_background_color, Color.GREEN);
	            setBackgroundResource(titleBarBackGround);
	            //�ȴ�����߰�ť
	            //��ȡ�Ƿ�Ҫ��ʾ��߰�ť
	            boolean leftButtonVisible = attributes.getBoolean(R.styleable.CustomTitleBar_left_button_visible, true);
	            if (leftButtonVisible) {
	                titleBarLeftBtn.setVisibility(View.VISIBLE);
	            } else {
	                titleBarLeftBtn.setVisibility(View.INVISIBLE);
	            }
	            //������߰�ť������
	            String leftButtonText = attributes.getString(R.styleable.CustomTitleBar_left_button_text);
	            if (!TextUtils.isEmpty(leftButtonText)) {
	                titleBarLeftBtn.setText(leftButtonText);
	                //������߰�ť������ɫ
	                int leftButtonTextColor = attributes.getColor(R.styleable.CustomTitleBar_left_button_text_color, Color.WHITE);
	                titleBarLeftBtn.setTextColor(leftButtonTextColor);
	            } else {
	                //�������ͼƬicon �����Ƕ�ѡһ Ҫôֻ�������� Ҫôֻ����ͼƬ
	                int leftButtonDrawable = attributes.getResourceId(R.styleable.CustomTitleBar_left_button_drawable, R.drawable.titlebar_back_icon);
	                if (leftButtonDrawable != -1) {
	                    titleBarLeftBtn.setBackgroundResource(leftButtonDrawable);
	                }
	            }

	            //�������
	            //�Ȼ�ȡ�����Ƿ�Ҫ��ʾͼƬicon
	            int titleTextDrawable = attributes.getResourceId(R.styleable.CustomTitleBar_title_text_drawable, -1);
	            if (titleTextDrawable != -1) {
	                titleBarTitle.setBackgroundResource(titleTextDrawable);
	            } else {
	                //�������ͼƬ���� ���ȡ���ֱ���
	                String titleText = attributes.getString(R.styleable.CustomTitleBar_title_text);
	                if (!TextUtils.isEmpty(titleText)) {
	                    titleBarTitle.setText(titleText);
	                }
	                //��ȡ������ʾ��ɫ
	                int titleTextColor = attributes.getColor(R.styleable.CustomTitleBar_title_text_color, Color.WHITE);
	                titleBarTitle.setTextColor(titleTextColor);
	            }

	            //�ȴ����ұ߰�ť
	            //��ȡ�Ƿ�Ҫ��ʾ�ұ߰�ť
	            boolean rightButtonVisible = attributes.getBoolean(R.styleable.CustomTitleBar_right_button_visible, true);
	            if (rightButtonVisible) {
	                titleBarRightBtn.setVisibility(View.VISIBLE);
	            } else {
	                titleBarRightBtn.setVisibility(View.INVISIBLE);
	            }
	            //�����ұ߰�ť������
	            String rightButtonText = attributes.getString(R.styleable.CustomTitleBar_right_button_text);
	            if (!TextUtils.isEmpty(rightButtonText)) {
	                titleBarRightBtn.setText(rightButtonText);
	                //�����ұ߰�ť������ɫ
	                int rightButtonTextColor = attributes.getColor(R.styleable.CustomTitleBar_right_button_text_color, Color.WHITE);
	                titleBarRightBtn.setTextColor(rightButtonTextColor);
	            } else {
	                //�����ұ�ͼƬicon �����Ƕ�ѡһ Ҫôֻ�������� Ҫôֻ����ͼƬ
	                int rightButtonDrawable = attributes.getResourceId(R.styleable.CustomTitleBar_right_button_drawable, -1);
	                if (rightButtonDrawable != -1) {
	                    titleBarRightBtn.setBackgroundResource(rightButtonDrawable);
	                }
	            }
	            attributes.recycle();
	        }
	    }

	    public void setTitleLeftBtnClickListener(OnClickListener onClickListener) {
	        if (onClickListener != null) {
	            titleBarLeftBtn.setOnClickListener(onClickListener);
	        }
	    }
	    
	    public void setTitleRightBtnClickListener(OnClickListener onClickListener) {
	        if (onClickListener != null) {
	            titleBarRightBtn.setOnClickListener(onClickListener);
	        }
	    }
	    
	    public Button getTitleBarLeftBtn() {
	        return titleBarLeftBtn;
	    }

	    public Button getTitleBarRightBtn() {
	        return titleBarRightBtn;
	    }

	    public TextView getTitleBarTitle() {
	        return titleBarTitle;
	    }
	}
