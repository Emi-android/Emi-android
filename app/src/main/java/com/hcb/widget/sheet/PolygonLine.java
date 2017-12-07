package com.hcb.widget.sheet;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class PolygonLine extends View {
    public PolygonLine(Context context) {
        super(context);
    }

    public PolygonLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PolygonLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PolygonLine(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private List<Point> points;
    private int color;
    private int lineW;
    private int pRadius = 8;

    public PolygonLine setPoints(List<Point> points) {
        this.points = points;
        return this;
    }

    public PolygonLine setLineColor(int color) {
        this.color = color;
        return this;
    }

    public PolygonLine setLineW(int lineW) {
        this.lineW = lineW;
        return this;
    }

    public PolygonLine setRadius(int radius) {
        this.pRadius = radius;
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == points || points.size() == 0) {
            return;
        }
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawCircle(points.get(0).x, points.get(0).y, pRadius, paint);
        if (points.size() > 1) {
            Path path = new Path();
            path.moveTo(points.get(0).x, points.get(0).y);
            for (int i = 1; i < points.size(); i++) {
                canvas.drawCircle(points.get(i).x, points.get(i).y, pRadius, paint);
                path.lineTo(points.get(i).x, points.get(i).y);
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineW);
            canvas.drawPath(path, paint);
        }
    }

}
