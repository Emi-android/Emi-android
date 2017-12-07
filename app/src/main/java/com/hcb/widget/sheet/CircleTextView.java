package com.hcb.widget.sheet;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.proj.emi.R;

public class CircleTextView extends TextView {

    public CircleTextView(Context context) {
        this(context, null);
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initColor(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initColor(context, attrs);
    }

    private void initColor(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleTextView);
        bgColor = a.getColor(R.styleable.CircleTextView_cvt_solid, bgColor);
    }

    private int bgColor;

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final Paint p = new Paint();
        p.setColor(bgColor);
        p.setAntiAlias(true);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, p);
        super.onDraw(canvas);
    }
}
