package com.xiaolian.amigo.ui.widget.swipebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.xiaolian.amigo.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.DimentionUtils;

/**
 * 滑动解锁
 * <p>
 * Created by zcd on 17/11/4.
 */

public class SlideUnlockView extends View {
    private static final String TAG = SlideUnlockView.class.getSimpleName();

    // 解锁的监听
    public interface OnUnLockListener {
        void setUnLocked(boolean lock);
    }

    // 滑块当前的状态
    public int currentState;
    // 未解锁
    public static final int STATE_LOCK = 1;
    // 解锁
    public static final int STATE_UNLOCK = 2;
    // 正在解锁中
    public static final int STATE_MOVING = 3;
    // 滑动解锁的背景图片
    private Drawable slideUnlockBackground;
    // 滑块的图片
    private Bitmap slideUnlockBlock;
    // 解锁后的滑块的图片
    private Bitmap slideEnabledUnlockBlock;
    // 未解锁时的图片
    private Bitmap slideDisableUnlockBlock;
    // 提示文字
    private Rect mTipsTextRect = new Rect();
    private String disableStr;
    private String enableStr;

    //    // 滑动解锁背景的宽度
//    private int blockBackgoundWidth;
    // 滑块宽度
    private int blockWidth;
    // 滑块高度
    private int blockHeight;
    private Paint mPaint;
    // 滑动坐标
    private float x;
    private float y;
    // 是否按在滑块上
    private boolean downOnBlock;
    // 通过handler来控制滑块在未解锁的时候，平缓的滑动到左端
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                // 如果x还大于0，就人为的设置缓慢移动到最左端，每次移动距离设置为背景宽的/100
                if (x > 0) {
                    x = x - getMeasuredWidth() * 1.0f / 100 * 5;
                    // 刷新界面
                    postInvalidate();
                    // 设置继续移动
                    handler.sendEmptyMessageDelayed(0, 5);
                } else {
                    handler.removeCallbacksAndMessages(null);
                    currentState = STATE_LOCK;
                    Log.i(TAG, "state---lock.....");
                }
            }
        }

    };

    // 解锁的监听
    private OnUnLockListener onUnLockListener;

    public SlideUnlockView(Context context) {
        this(context, null);
    }

    public SlideUnlockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideUnlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 默认滑动解锁为未解锁状态
        currentState = STATE_LOCK;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideUnlockView);
        int slideUnlockBackgroundResource = typedArray.getResourceId(R.styleable.SlideUnlockView_slideUnlockBackgroundResource,
                0);
        int slideUnlockBlockResource = typedArray
                .getResourceId(R.styleable.SlideUnlockView_slideUnlockBlockResource, 0);
        int slideEnableUnlockBlockResource = typedArray
                .getResourceId(R.styleable.SlideUnlockView_slideEnabledUnlockBlockResource, 0);
        int slideDisableUnlockBlockResource = typedArray
                .getResourceId(R.styleable.SlideUnlockView_slideDisableUnlockBlockResource, 0);
        disableStr = typedArray.getString(R.styleable.SlideUnlockView_slideDisableText);
        enableStr = typedArray.getString(R.styleable.SlideUnlockView_slideEnableText);
        typedArray.recycle();
        // 取出自定义属性中当前状态
        // 如果解锁状态是true，说明已经解锁
        // 当取出自定义属性的背景时，设置背景
        setSlideUnlockBackground(slideUnlockBackgroundResource);
        // 当取出自定义属性的滑块时，设置滑块的图片
        setSlideUnlockBlock(slideUnlockBlockResource);
        setSlideEnabledUnlockBlock(slideEnableUnlockBlockResource);
        setSlideDisableUnlockBlock(slideDisableUnlockBlockResource);
        // 执行onDraw方法，进行界面绘制
        postInvalidate();

    }

    private void setSlideDisableUnlockBlock(int slideDisableUnlockBlockResource) {
        slideDisableUnlockBlock = BitmapFactory.decodeResource(getResources(),
                slideDisableUnlockBlockResource);

    }

    private void setSlideEnabledUnlockBlock(int slideEnableUnlockBlockResource) {
        slideEnabledUnlockBlock = BitmapFactory.decodeResource(getResources(),
                slideEnableUnlockBlockResource);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 在一开始的使用将背景图绘制出来
//        canvas.drawBitmap(slideUnlockBackground, 0, 0, null);
        int unlockX = getMeasuredWidth() - blockWidth;
        /*
         * 判断当前状态
         */
        switch (currentState) {
            // 如果是未解锁，就将滑块绘制到最左端
            case STATE_LOCK:
                drawText(canvas, disableStr, R.color.colorDark6);
                canvas.drawBitmap(slideDisableUnlockBlock, unlockX, 0, null);
                canvas.drawBitmap(slideUnlockBlock, 0, 0, null);
                break;
            // 已解锁，计算出
            case STATE_UNLOCK:
                canvas.drawBitmap(slideEnabledUnlockBlock, unlockX, 0, null);
                if (mPaint == null) {
                    mPaint = new Paint();
                }
                mPaint.setColor(getResources().getColor(R.color.colorFullRed));
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(0, 0, unlockX, blockHeight, mPaint);
                drawText(canvas, enableStr, R.color.white);
                break;
            case STATE_MOVING:
                drawText(canvas, disableStr, R.color.colorDark6);
                canvas.drawBitmap(slideDisableUnlockBlock, unlockX, 0, null);
                if (x < 0) {
                    x = 0;
                } else if (x > getMeasuredWidth() - blockWidth) {
                    x = getMeasuredWidth() - blockWidth;
                }
                canvas.drawBitmap(slideUnlockBlock, x, 0, null);
                if (mPaint == null) {
                    mPaint = new Paint();
                }
                mPaint.setColor(getResources().getColor(R.color.colorFullRed));
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(0, 0, x, blockHeight, mPaint);
                break;
            default:
                break;
        }
    }

    private void drawText(Canvas canvas, String text, int color) {
        Paint mPaint = new Paint();
//        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        mPaint.setTextSize(DimentionUtils.convertSpToPixels(14, getContext()));
        mPaint.setColor(getResources().getColor(color));
        mPaint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(text, getMeasuredWidth() / 2 - bounds.width() / 2, baseline, mPaint);
    }

    public void setSlideUnlockBackground(int slideUnlockBackgroundResource) {
        Log.i(TAG, "setSlideUnlockBackground.....");
//        slideUnlockBackground = BitmapFactory.decodeResource(getResources(),
//                slideUnlockBackgroundResource).getWidth();
        // 获取背景图的宽和高
//        blockBackgoundWidth = slideUnlockBackground.getWidth();

    }

    public void setSlideUnlockBlock(int slideUnlockBlockResource) {
        Log.i(TAG, "setSlideUnlockBlock.....");
        slideUnlockBlock = BitmapFactory.decodeResource(getResources(),
                slideUnlockBlockResource);
        // 获取滑块的宽和高
        blockWidth = slideUnlockBlock.getWidth();
        blockHeight = slideUnlockBlock.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.i(TAG, "onMeauser.....");
//        setMeasuredDimension(slideUnlockBackground.getWidth(),
//                slideUnlockBackground.getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            // 当手指按下的时候，判断手指按下的位置是否在滑块上边
            case MotionEvent.ACTION_DOWN:

                if (currentState != STATE_MOVING) {
                    // 判断一下，如果当前正在移动，则不执行触摸操作
                    // 获取相对于背景的左上角的x，y值
                    x = event.getX();
                    y = event.getY();
                    // 先计算出滑块的中心点的x，y坐标
                    float blockCenterX = blockWidth * 1.0f / 2;
                    float blockCenterY = blockHeight * 1.0f / 2;
                    downOnBlock = isDownOnBlock(blockCenterX, x, blockCenterY, y);
                    Log.i(TAG, "down......................");
                    // 调用onDraw方法
                    postInvalidate();

                }
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果手指确定按在滑块上，就视为开始拖拽滑块
                if (downOnBlock) {
                    // 获取相对于背景的左上角的x，y值
                    x = event.getX();
                    y = event.getY();
                    currentState = STATE_MOVING;
//                    Log.i(TAG, "move......................");
                    // 调用onDraw方法
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentState == STATE_MOVING) {
                    // 当手指抬起的时候，应该是让滑块归位的
                    // 说明未解锁
                    if (x < getMeasuredWidth() - blockWidth) {
                        handler.sendEmptyMessageDelayed(0, 10);
                        // 通过回调设置已解锁
                        onUnLockListener.setUnLocked(false);
                    } else {
                        currentState = STATE_UNLOCK;
                        // 通过回调设置未解锁
                        onUnLockListener.setUnLocked(true);
                    }
                    downOnBlock = false;
                    // 调用onDraw方法
                    postInvalidate();

                }
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * 计算手指是否是落在了滑块上(默认是按照滑块在未解锁的初始位置来计算的)
     */
    public boolean isDownOnBlock(float x1, float x2, float y1, float y2) {
        float sqrt = (float) Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2)
                + Math.abs(y1 - y2) * Math.abs(y1 - y2));
        return sqrt <= blockWidth / 2;
    }

    /**
     * 设置解锁监听
     */
    public void setOnUnLockListener(OnUnLockListener onUnLockListener) {
        this.onUnLockListener = onUnLockListener;
    }

    public void setDisableStr(String disableStr) {
        this.disableStr = disableStr;
    }

    public void setEnableStr(String enableStr) {
        this.enableStr = enableStr;
    }

    /**
     * 重置一下滑动的状态，保证下次能够正常使用
     */
    public void reset() {
        currentState = STATE_LOCK;
        postInvalidate();
    }

    public void setUnlock() {
        currentState = STATE_UNLOCK;
        postInvalidate();
    }

    //判断手指是否在滑块的背景区域移动
    public boolean isOnBackground(int x, int y) {
        return x <= getMeasuredWidth() && y <= getMeasuredHeight();
    }
}
