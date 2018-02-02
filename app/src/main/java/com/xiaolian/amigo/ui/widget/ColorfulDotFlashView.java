package com.xiaolian.amigo.ui.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ScreenUtils;

/**
 * 下拉刷新动画
 *
 * @author zcd
 * @date 17/9/26
 */

public class ColorfulDotFlashView extends View {
    private int firstColor, secondColor;
    private int mWidth, mHeight;
    private Paint mPaint;
    private int number = 0;
    private static int DOT_NUM = 3;
    private int colors[] = {
            R.color.refresh_green,
            R.color.refresh_red,
            R.color.refresh_blue
    };
    private int mRadius;
    private int mBigRadius;
    private ValueAnimator valueAnimator;
    private int margin;

    public ColorfulDotFlashView(Context context) {
        this(context, null);
    }

    public ColorfulDotFlashView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfulDotFlashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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
//        mRadius = ScreenUtils.pxToDpCeilInt(getContext(), 12);
        mRadius = ScreenUtils.dpToPxInt(getContext(), 2);
        mBigRadius = ScreenUtils.dpToPxInt(getContext(), 3);
        margin = ScreenUtils.dpToPxInt(getContext(), 14);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画布原点移到中心
        canvas.translate(mWidth / 2, mHeight / 2);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < DOT_NUM; i++) {
            int color = ContextCompat.getColor(getContext(), colors[i]);
            if (i == number % DOT_NUM) {
                mPaint.setColor(color);
                canvas.drawCircle((i - 1) * margin, 0, mBigRadius, mPaint);
            } else {
                mPaint.setColor(color);
                mPaint.setAlpha(40);
                canvas.drawCircle((i - 1) * margin, 0, mRadius, mPaint);
            }

        }
    }

    public void startAnimation() {


        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(mHeight / 2, mHeight / 8, mHeight / 2);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    invalidate();
                }

            });
            //动画循环执行
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setDuration(200);
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
        } else {
            valueAnimator.start();
        }

    }

    public void endAnimation() {
        if (valueAnimator != null) {
            number = 0;
            valueAnimator.cancel();
            valueAnimator.end();
        }

    }
}
