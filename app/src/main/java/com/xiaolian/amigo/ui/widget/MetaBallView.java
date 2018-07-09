package com.xiaolian.amigo.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xiaolian.amigo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zcd
 * @date 18/7/9
 */
public class MetaBallView extends LinearLayout {
    @BindView(R.id.ll_left)
    LinearLayout llLeft;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    private float viewWidth;
    private float llWidth;
    private float llHeight;
    private boolean animating = false;
    private Path mPath;
    private Paint mPaint;
    private Context context;

    public MetaBallView(Context context) {
        super(context);
        init(context);
    }

    public MetaBallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MetaBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_meta_ball, this, true);
        ButterKnife.bind(this, view);
        view.post(() -> viewWidth = view.getMeasuredWidth());
        view.post(() -> llWidth = llRight.getMeasuredWidth());
        view.post(() -> llHeight = llRight.getMeasuredHeight());
    }

    public void translation() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(context, R.color.colorFullRed));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        float currentLeftX = llLeft.getTranslationX();
        float currentRightX = llRight.getTranslationX();
        ObjectAnimator animatorLeft = ObjectAnimator.ofFloat(llLeft, "translationX",
                currentLeftX, viewWidth / 4);
        ObjectAnimator animatorRight = ObjectAnimator.ofFloat(llRight, "translationX",
                currentRightX, -viewWidth / 4);
        animatorLeft.setDuration(2000);
        animatorRight.setDuration(2000);
        animatorLeft.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getCurrentPlayTime() > 1000) {
                    postInvalidate();
                }
            }
        });
        animatorLeft.start();
        animatorRight.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (animating) {
            float mCircleOneX = llLeft.getTranslationX() + llWidth / 2;
            float mCircleOneY = llLeft.getTranslationY() + llHeight;
            float mCircleTwoX = llRight.getTranslationX() + viewWidth / 2 + llWidth / 2;
            float mCircleTwoY = llRight.getTranslationY() + llHeight;
            float x = mCircleTwoX;
            float y = mCircleTwoY;
            float startX = mCircleOneX;
            float startY = mCircleOneY;
            float controlX = (startX + x) / 2;
            float controlY = (startY + y) / 2;

            float mRadiusNormal = llHeight / 2 ;

            float distance = (float) Math.sqrt((controlX - startX) * (controlX - startX) + (controlY - startY) * (controlY - startY));
            double a = Math.acos(mRadiusNormal / distance);

            double b = Math.acos((controlX - startX) / distance);
            float offsetX1 = (float) (mRadiusNormal * Math.cos(a - b));
            float offsetY1 = (float) (mRadiusNormal * Math.sin(a - b));
            float tanX1 = startX + offsetX1;
            float tanY1 = startY - offsetY1;

            double c = Math.acos((controlY - startY) / distance);
            float offsetX2 = (float) (mRadiusNormal * Math.sin(a - c));
            float offsetY2 = (float) (mRadiusNormal * Math.cos(a - c));
            float tanX2 = startX - offsetX2;
            float tanY2 = startY + offsetY2;

            double d = Math.acos((y - controlY) / distance);
            float offsetX3 = (float) (mRadiusNormal * Math.sin(a - d));
            float offsetY3 = (float) (mRadiusNormal * Math.cos(a - d));
            float tanX3 = x + offsetX3;
            float tanY3 = y - offsetY3;

            double e = Math.acos((x - controlX) / distance);
            float offsetX4 = (float) (mRadiusNormal * Math.cos(a - e));
            float offsetY4 = (float) (mRadiusNormal * Math.sin(a - e));
            float tanX4 = x - offsetX4;
            float tanY4 = y + offsetY4;

            mPath.reset();
            mPath.moveTo(tanX1, tanY1);
            mPath.quadTo(controlX, controlY, tanX3, tanY3);
            mPath.lineTo(tanX4, tanY4);
            mPath.quadTo(controlX, controlY, tanX2, tanY2);
            canvas.drawPath(mPath, mPaint);

            // 辅助线
//            canvas.drawCircle(tanX1, tanY1, 5, mPaint);
//            canvas.drawCircle(tanX2, tanY2, 5, mPaint);
//            canvas.drawCircle(tanX3, tanY3, 5, mPaint);
//            canvas.drawCircle(tanX4, tanY4, 5, mPaint);
//            canvas.drawLine(mCircleOneX, mCircleOneY, mCircleTwoX, mCircleTwoY, mPaint);
//            canvas.drawLine(0, mCircleOneY, mCircleOneX + mRadiusNormal + 400, mCircleOneY, mPaint);
//            canvas.drawLine(mCircleOneX, 0, mCircleOneX, mCircleOneY + mRadiusNormal + 50, mPaint);
//            canvas.drawLine(mCircleTwoX, mCircleTwoY, mCircleTwoX, 0, mPaint);
//            canvas.drawCircle(controlX, controlY, 5, mPaint);
//            canvas.drawCircle(mCircleOneX, mCircleOneY, 5, mPaint);
//            canvas.drawCircle(mCircleTwoX, mCircleTwoY, 5, mPaint);
//            canvas.drawLine(startX, startY, tanX1, tanY1, mPaint);
//            canvas.drawLine(tanX1, tanY1, controlX, controlY, mPaint);
        } else {
        }
        super.onDraw(canvas);
    }
}
