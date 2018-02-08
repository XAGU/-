
package com.xiaolian.amigo.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ScreenUtils;

/**
 * 波浪view
 *
 * @author zcd
 * @date 17/9/13
 */

public class BezierWaveView extends View {
    /**
     * 毛边过滤
     */
    public PaintFlagsDrawFilter mPaintFlagsDrawFilter;
    private Paint mPaint1;
    private Paint mPaint2;
    private Paint mPaint3;

    private Path mPath1;
    private Path mPath2;
    private Path mPath3;

    ValueAnimator mAnimator1;
    ValueAnimator mAnimator2;
    ValueAnimator mAnimator3;


    private int mDisplacement1 = 0;
    private int mDisplacement2 = 0;
    private int mDisplacement3 = 0;

    private int mItemWaveWidth1 = 1000;
    private int mItemWaveWidth2 = 1200;
    private int mItemWaveWidth3 = 1400;
    private int mWaveHeight = 100;


    public BezierWaveView(Context context) {
        this(context, null);
    }

    public BezierWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mItemWaveWidth1 = ScreenUtils.getScreenWidth(context);
        mItemWaveWidth2 = mItemWaveWidth1;
        mItemWaveWidth3 = mItemWaveWidth1;
        mDisplacement3 = mItemWaveWidth1 / 4;
        initPaint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath1.reset();
        mPath2.reset();
        mPath3.reset();


        mPath1.moveTo(-mItemWaveWidth1 + mDisplacement1, mWaveHeight);
        mPath2.moveTo(-mItemWaveWidth2 + mDisplacement2, mWaveHeight);
        mPath3.moveTo(-mItemWaveWidth3 + mDisplacement3, mWaveHeight);


        for (int i = -mItemWaveWidth1; i <= getWidth() + mItemWaveWidth1; i += mItemWaveWidth1) {
            mPath1.rQuadTo(mItemWaveWidth1 / 4, -mWaveHeight, mItemWaveWidth1 / 2, 5);
            mPath1.rQuadTo(mItemWaveWidth1 / 4, mWaveHeight, mItemWaveWidth1 / 2, -5);
        }

        for (int i = -mItemWaveWidth2; i <= getWidth() + mItemWaveWidth2; i += mItemWaveWidth2) {
            mPath2.rQuadTo(mItemWaveWidth2 / 4, mWaveHeight, mItemWaveWidth2 / 2, 10);
            mPath2.rQuadTo(mItemWaveWidth2 / 4, -mWaveHeight, mItemWaveWidth2 / 2, -10);
        }

        for (int i = -mItemWaveWidth3; i <= getWidth() + mItemWaveWidth3; i += mItemWaveWidth3) {
            mPath3.rQuadTo(mItemWaveWidth3 / 4, mWaveHeight, mItemWaveWidth3 / 2, 0);
            mPath3.rQuadTo(mItemWaveWidth3 / 4, -mWaveHeight, mItemWaveWidth3 / 2, 0);
        }

        mPath1.lineTo(getWidth(), getHeight());
        mPath1.lineTo(0, getHeight());
        mPath1.close();

        mPath2.lineTo(getWidth(), getHeight());
        mPath2.lineTo(0, getHeight());
        mPath2.close();

        mPath3.lineTo(getWidth(), getHeight());
        mPath3.lineTo(0, getHeight());
        mPath3.close();

        canvas.drawPath(mPath1, mPaint1);
        canvas.drawPath(mPath2, mPaint2);
        canvas.drawPath(mPath3, mPaint3);
    }


    private void initPaint() {
        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0,
                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        mPaint1 = new Paint();
        mPaint2 = new Paint();
        mPaint3 = new Paint();

        mPath1 = new Path();
        mPath2 = new Path();
        mPath3 = new Path();

        mPaint1.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint3.setStyle(Paint.Style.FILL_AND_STROKE);

        mPaint1.setAntiAlias(true);
        mPaint2.setAntiAlias(true);
        mPaint3.setAntiAlias(true);

        mPaint1.setColor(getResources().getColor(R.color.white20));
        mPaint2.setColor(getResources().getColor(R.color.white50));
        mPaint3.setColor(getResources().getColor(R.color.white80));
    }


    public void startAnim() {
        mAnimator1 = ValueAnimator.ofInt(0, mItemWaveWidth1);
        mAnimator1.setDuration(6500);
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
        mAnimator2.setDuration(6500);
        mAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator2.setInterpolator(new LinearInterpolator());
        mAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDisplacement2 = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        mAnimator3 = ValueAnimator.ofInt(-mItemWaveWidth3 / 4, mItemWaveWidth3 * 3 / 4);
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

        mAnimator1.start();
        mAnimator2.start();
        mAnimator3.start();
    }

    public boolean isRunning() {
        if (mAnimator1 != null) {
            return mAnimator1.isRunning();
        } else {
            return false;
        }
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        super.onWindowFocusChanged(hasWindowFocus);
//        if (!hasWindowFocus) {
//            endAnimation();
//        }
//    }

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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        endAnimation();
    }

//    @Override
//    protected void onWindowVisibilityChanged(int visibility) {
//        super.onWindowVisibilityChanged(visibility);
//        if (visibility != VISIBLE) {
//            endAnimation();
//        }
//    }

}
