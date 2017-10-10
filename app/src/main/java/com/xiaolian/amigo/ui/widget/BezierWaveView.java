package com.xiaolian.amigo.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.xiaolian.amigo.R;

/**
 * 波浪view
 * @author zcd
 */

public class BezierWaveView extends View {
    public PaintFlagsDrawFilter mPaintFlagsDrawFilter;// 毛边过滤
    private Paint mPaint1;
    private Paint mPaint2;
    private Paint mPaint3;
//    private Paint mPaint4;

    private Path mPath1;
    private Path mPath2;
    private Path mPath3;
//    private Path mPath4;

    ValueAnimator mAnimator1;
    ValueAnimator mAnimator2;
    ValueAnimator mAnimator3;
//    ValueAnimator mAnimator4;


    private int mDisplacement1 = 0;
    private int mDisplacement2 = 0;
    private int mDisplacement3 = 0;
//    private int mDisplacement4 = 0;

    private int mItemWaveWidth1 = 1200;
    private int mItemWaveWidth2 = 1050;
    private int mItemWaveWidth3 = 1400;
//    private int mItemWaveWidth4 = 1100;


    public BezierWaveView(Context context) {
        this(context, null);
    }

    public BezierWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath1.reset();
        mPath2.reset();
        mPath3.reset();
//        mPath4.reset();


        mPath1.moveTo(-mItemWaveWidth1 + mDisplacement1, 67);
        mPath2.moveTo(-mItemWaveWidth2 + mDisplacement2, 77);
        mPath3.moveTo(-mItemWaveWidth3 + mDisplacement3, 70);
//        mPath4.moveTo(-mItemWaveWidth4 + mDisplacement4, 63);


        for (int i = -mItemWaveWidth1; i <= getWidth() + mItemWaveWidth1; i += mItemWaveWidth1) {
            mPath1.rQuadTo(280, -76, mItemWaveWidth1 / 2, 5);
            mPath1.rQuadTo(280, 76, mItemWaveWidth1 / 2, -5);
        }

        for (int i = -mItemWaveWidth2; i <= getWidth() + mItemWaveWidth2; i += mItemWaveWidth2) {
            mPath2.rQuadTo(240, 70, mItemWaveWidth2 / 2, 8);
            mPath2.rQuadTo(240, -70, mItemWaveWidth2 / 2, -8);
        }

        for (int i = -mItemWaveWidth3; i <= getWidth() + mItemWaveWidth3; i += mItemWaveWidth3) {
            mPath3.rQuadTo(380, 80, mItemWaveWidth3 / 2, 15);
            mPath3.rQuadTo(380, -80, mItemWaveWidth3 / 2, -15);
        }

//        for (int i = -mItemWaveWidth4; i <= getWidth() + mItemWaveWidth4; i += mItemWaveWidth4) {
//            mPath4.rQuadTo(270, -80, mItemWaveWidth4 / 2, 17);
//            mPath4.rQuadTo(270, 80, mItemWaveWidth4 / 2, -17);
//        }

        mPath1.lineTo(getWidth(), getHeight());
        mPath1.lineTo(0, getHeight());
        mPath1.close();

        mPath2.lineTo(getWidth(), getHeight());
        mPath2.lineTo(0, getHeight());
        mPath2.close();

        mPath3.lineTo(getWidth(), getHeight());
        mPath3.lineTo(0, getHeight());
        mPath3.close();

//        mPath4.lineTo(getWidth(), getHeight());
//        mPath4.lineTo(0, getHeight());
//        mPath4.close();

        canvas.drawPath(mPath1, mPaint1);
        canvas.drawPath(mPath2, mPaint2);
        canvas.drawPath(mPath3, mPaint3);
//        canvas.drawPath(mPath4, mPaint4);
    }


    private void initPaint() {
        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0,
                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        mPaint1 = new Paint();
        mPaint2 = new Paint();
        mPaint3 = new Paint();
//        mPaint4 = new Paint();

        mPath1 = new Path();
        mPath2 = new Path();
        mPath3 = new Path();
//        mPath4 = new Path();

        mPaint1.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint3.setStyle(Paint.Style.FILL_AND_STROKE);
//        mPaint4.setStyle(Paint.Style.FILL_AND_STROKE);

        mPaint1.setColor(getResources().getColor(R.color.white80));
        mPaint2.setColor(getResources().getColor(R.color.white80));
        mPaint3.setColor(getResources().getColor(R.color.white80));
//        mPaint4.setColor(getResources().getColor(R.color.white70));
    }


    public void startAnim() {
        mAnimator1 = ValueAnimator.ofInt(0, mItemWaveWidth1);
        mAnimator1.setDuration(7950);
        mAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator1.setInterpolator(new LinearInterpolator());
        mAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDisplacement1 = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });


        mAnimator2 = ValueAnimator.ofInt(0, mItemWaveWidth2);
        mAnimator2.setDuration(7400);
        mAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator2.setInterpolator(new LinearInterpolator());
        mAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDisplacement2 = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        mAnimator3 = ValueAnimator.ofInt(0, mItemWaveWidth3);
        mAnimator3.setDuration(6500);
        mAnimator3.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator3.setInterpolator(new LinearInterpolator());
        mAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDisplacement3 = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

//        mAnimator4 = ValueAnimator.ofInt(0, mItemWaveWidth4);
//        mAnimator4.setDuration(8700);
//        mAnimator4.setRepeatCount(ValueAnimator.INFINITE);
//        mAnimator4.setInterpolator(new LinearInterpolator());
//        mAnimator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                mDisplacement4 = (int) animation.getAnimatedValue();
//                postInvalidate();
//            }
//        });


        mAnimator1.start();
        mAnimator2.start();
        mAnimator3.start();
//        mAnimator4.start();
    }

    public boolean isRunning() {
        if (mAnimator1 != null) {
            return mAnimator1.isRunning();
        } else {
            return false;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            endAnimation();
        }
    }

    private void endAnimation() {
        if (mAnimator1 != null) {
            mAnimator1.cancel();
            mAnimator1.end();
        }
        if (mAnimator2 != null) {
            mAnimator2.cancel();
            mAnimator2.end();
        }
        if (mAnimator3 != null) {
            mAnimator3.cancel();
            mAnimator3.end();
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility != VISIBLE) {
            endAnimation();
        }
    }

}
