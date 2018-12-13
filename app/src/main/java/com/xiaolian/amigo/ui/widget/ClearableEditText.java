package com.xiaolian.amigo.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.Log;

/**
 * 自定义密码输入框，支持显示、隐藏密码
 *
 * @author caidong
 * @date 17/8/31
 */
public class ClearableEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {


    private static final String TAG = ClearableEditText.class.getSimpleName();
    /**
     * 右侧Drawable引入
     */
    private Drawable mDrawableRight;
    /**
     * 控件是否有焦点
     */
    private boolean hasFocus;

    boolean validated = false;

    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;
    private Context context ;

    private int maxLength ;

    // 构造方法 1->2->3
    public ClearableEditText(Context context) {
        this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context ;
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
        maxLength = getMaxHeight() ;
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
//        if (!resetText) {
//            cursorPos = getSelectionEnd();
//            // 这里用s.toString()而不直接用s是因为如果用s，
//            // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
//            // inputAfterText也就改变了，那么表情过滤就失败了
//            inputAfterText= s.toString();
//        }
    }



    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(s, start, lengthBefore, lengthAfter);
        if (hasFocus) {
            setClearIconVisible(s.length() > 0);
        }

//        if (!resetText) {
//            if (lengthAfter >= 2) {//表情符号的字符长度最小为2
//
//                Log.wtf(TAG , lengthAfter +" " +"  " + start  + "   " + lengthBefore) ;
//                CharSequence input ;
//                if (cursorPos + lengthAfter < maxLength){
//                    input  = s.subSequence(cursorPos, cursorPos + lengthAfter);
//                }else{
//                    input = s.subSequence(cursorPos , maxLength);
//                }
//
//                if (containsEmoji(input.toString())) {
//                    resetText = true;
////                    Toast.makeText(context, "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
//                    //是表情符号就将文本还原为输入表情符号之前的内容
//                    setText(inputAfterText);
//                    CharSequence text = getText();
//                    if (text instanceof Spannable) {
//                        Spannable spanText = (Spannable) text;
//                        Selection.setSelection(spanText, text.length());
//                    }
//                }
//            }
//        } else {
//            resetText = false;
//        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }


}
