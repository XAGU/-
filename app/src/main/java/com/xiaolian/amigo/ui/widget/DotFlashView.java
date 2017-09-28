package com.xiaolian.amigo.ui.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ScreenUtils;

/**
 * 设备重连加载View
 * @author zcd
 */

public class DotFlashView extends View {
    private int firstColor, secondColor;
    private int mWidth, mHeight;
    private Paint mPaint;
    private int number = 0;
    private int DOT_NUM = 3;
    private int mRadius;
    private ValueAnimator valueAnimator;

    public DotFlashView(Context context) {
        this(context, null);
    }

    public DotFlashView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotFlashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DotFlashView, defStyleAttr, 0);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {

                case R.styleable.DotFlashView_firstColor:
                    // 默认颜色设置为黑色
                    firstColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.DotFlashView_secondColor:
                    secondColor = array.getColor(attr, Color.BLACK);
                    break;
            }
        }
        init();
        array.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = widthSize / 2;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = heightSize / 2;
        }
        mWidth = width;
        mHeight = height;
//        mRadius = mHeight / 2;
        mRadius = ScreenUtils.pxToDpCeilInt(getContext(), 12);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画布原点移到中心
        canvas.translate(mWidth / 2, mHeight / 2);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < DOT_NUM; i++) {
            if (i == number % DOT_NUM) {
                mPaint.setColor(secondColor);
                canvas.drawCircle((i - 1) * mHeight, 0, mRadius, mPaint);
            } else {
                mPaint.setColor(firstColor);
                canvas.drawCircle((i - 1) * mHeight, 0, mRadius, mPaint);
            }

        }
    }

    public void startAnimation(){


        valueAnimator = ValueAnimator.ofInt(mHeight / 2, mHeight / 8, mHeight / 2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }

        });
        //动画循环执行
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(500);
        valueAnimator.start();

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                number++;
            }
        });

    }

    public void endAnimation() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator.end();
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            endAnimation();
        }
    }
}
