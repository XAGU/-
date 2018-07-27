package com.xiaolian.amigo.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zcd
 * @date 18/7/9
 */
public class MetaBallView extends LinearLayout {
    public interface OnButtonClickListener {
        void onButtonClick(boolean left);
    }
    @BindView(R.id.ll_left)
    LinearLayout llLeft;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_missed_booking_time)
    TextView tvMissedBookingTime;
    private float viewWidth;
    private float llWidth;
    private float llHeight;
    private boolean animating = false;
    private Path mPath;
    private Paint mPaint;
    private Context context;
    private OnButtonClickListener listener;

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

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_meta_ball, this, true);
        ButterKnife.bind(this, view);
        view.post(() -> viewWidth = view.getMeasuredWidth());
        view.post(() -> llWidth = llRight.getMeasuredWidth());
        view.post(() -> llHeight = llRight.getMeasuredHeight());
        llRight.setOnClickListener(v -> {
            if (listener != null) {
                listener.onButtonClick(false);
            }
        });
        llLeft.setOnClickListener(v -> {
            if (listener != null) {
                listener.onButtonClick(true);
            }
        });
    }

    public LinearLayout getLlLeft() {
        return llLeft;
    }

    public LinearLayout getLlRight() {
        return llRight;
    }

    /**
     * 显示购买编码
     */
    public void changeToBuyCodeWay() {
        tvLeft.setText("洗澡卷");
        ivLeft.setImageResource(R.drawable.ic_bathroom_buy_code);
        tvRight.setText("扫一扫");
        ivRight.setImageResource(R.drawable.ic_bathroom_scan);
    }

    /**
     * 显示预约使用
     */
    public void changeToBookingWay() {
        tvLeft.setText("预约使用");
        ivLeft.setImageResource(R.drawable.ic_bathroom_booking);
        tvRight.setText("付费使用");
        ivRight.setImageResource(R.drawable.ic_bathroom_pay_use);
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
        animatorLeft.setDuration(1000);
        animatorRight.setDuration(1000);
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
        animatorLeft.addUpdateListener(animation -> postInvalidate());
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

            float mRadiusNormal = llHeight / 2;

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
