package com.xiaolian.amigo.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xiaolian.amigo.R;

/**
 * 自定义密码输入框，支持显示、隐藏密码
 *
 * @author caidong
 * @date 17/8/31
 */
public class ClearableEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    /**
     * 右侧Drawable引入
     */
    private Drawable mDrawableRight;
    /**
     * 控件是否有焦点
     */
    private boolean hasFocus;

    boolean validated = false;

    // 构造方法 1->2->3
    public ClearableEditText(Context context) {
        this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initClearDrawable();
        setClearIconVisible(false);
    }


    private void initClearDrawable() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mDrawableRight = getCompoundDrawables()[2];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDrawableRight = getResources().getDrawable(R.drawable.clear, null);
        } else {
            mDrawableRight = getResources().getDrawable(R.drawable.clear);
        }

        mDrawableRight.setBounds(0, 0, mDrawableRight.getIntrinsicWidth(), mDrawableRight.getIntrinsicHeight());

        setClearIconVisible(true);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    private void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mDrawableRight : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mDrawableRight != null && event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            // 判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight()))
                    && (x < (getWidth() - getPaddingRight()));
            // 获取删除图标的边界，返回一个Rect对象
            Rect rect = mDrawableRight.getBounds();

            /*
            // 获取删除图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            // 计算图标底部到控件底部的距离
            int distance = (getHeight() - height) / 2;
            // 判断触摸点是否在竖直范围内(可能会有点误差)
            // 触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标

            */
            // boolean isInnerHeight = (y > distance) && (y < (distance + height));
            if (isInnerWidth) {
                this.setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (hasFocus) {
            setClearIconVisible(text.length() > 0);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}
