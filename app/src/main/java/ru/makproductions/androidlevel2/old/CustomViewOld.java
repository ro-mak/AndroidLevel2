package ru.makproductions.androidlevel2.old;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomViewOld extends View {

    private Paint paint;
    private Paint paint2;
    private View.OnClickListener listener;
    private boolean pressed;

    public CustomViewOld(Context context) {
        super(context);
        initPaint();
    }

    public CustomViewOld(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CustomViewOld(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @TargetApi(21)
    public CustomViewOld(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }

    private void initPaint(){
        paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.FILL);
        paint2 = new Paint();
        paint2.setColor(Color.BLACK);
        paint2.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(200, 200, 400, 400, paint);
        if (pressed) {
            canvas.drawLine(0, 0, 200, 200, paint2);
            canvas.drawLine(200, 0, 400, 200, paint2);
            canvas.drawLine(0, 200, 200, 400, paint2);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            pressed = true;
            invalidate();
            if (listener != null) listener.onClick(this);
        } else if (action == MotionEvent.ACTION_UP) {
            pressed = false;
            invalidate();
        }
        return true;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.listener = l;
    }
}
