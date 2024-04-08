package com.example.nodemcuwificontrollercarapp.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class JoystickView extends View {

    private float centerX;
    private float centerY;
    private float joystickRadius;
    private float joystickCenterX;
    private float joystickCenterY;
    private float joystickCenterRadius;

    private Paint paint;

    public JoystickView(Context context) {
        super(context);
        initJoystickView();
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initJoystickView();
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initJoystickView();
    }

    private void initJoystickView() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        centerX = w / 2f;
        centerY = h / 2f;
        joystickRadius = w / 3f;
        joystickCenterX = centerX;
        joystickCenterY = centerY;
        joystickCenterRadius = joystickRadius / 4;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Draw joystick background circle
        paint.setColor(Color.GRAY);
        canvas.drawCircle(centerX, centerY, joystickRadius, paint);

        // Draw joystick control circle
        paint.setColor(Color.GREEN);
        canvas.drawCircle(joystickCenterX, joystickCenterY, joystickCenterRadius, paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                updateJoystickPosition(x, y);
                break;
            case MotionEvent.ACTION_UP:
                resetJoystickPosition();
                break;
        }
        return true;
    }

    private void updateJoystickPosition(float x, float y) {
        joystickCenterX = x;
        joystickCenterY = y;

        double distanceFromCenter = Math.sqrt(Math.pow(joystickCenterX - centerX, 2)
                + Math.pow(joystickCenterY - centerY, 2));

        if (distanceFromCenter > joystickRadius) {
            double angle = Math.atan2(joystickCenterY - centerY, joystickCenterX - centerX);
            joystickCenterX = (float) (centerX + joystickRadius * Math.cos(angle));
            joystickCenterY = (float) (centerY + joystickRadius * Math.sin(angle));
        }

        invalidate();
    }

    private void resetJoystickPosition() {
        joystickCenterX = centerX;
        joystickCenterY = centerY;
        invalidate();
    }

    public void setOnJoystickMovedListener(JoystickView.OnJoystickMovedListener onJoystickMovedListener) {
    }

    public interface OnJoystickMovedListener {
        void onJoystickMoved(float xPercent, float yPercent);
    }
}