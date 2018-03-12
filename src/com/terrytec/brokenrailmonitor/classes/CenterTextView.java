package com.terrytec.brokenrailmonitor.classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

public class CenterTextView extends TextView {
    private StaticLayout mStaticLayout;
    private TextPaint mTextPaint;

    public CenterTextView(Context context) {
        super(context);
    }

    public CenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initView();
    }

    private void initView() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(getTextSize());
        mTextPaint.setColor(getCurrentTextColor());
        mStaticLayout = new StaticLayout(getText(), mTextPaint, getWidth(), StaticLayout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mTextPaint.setColor(getCurrentTextColor());
        mStaticLayout.draw(canvas);
    }
    
}
