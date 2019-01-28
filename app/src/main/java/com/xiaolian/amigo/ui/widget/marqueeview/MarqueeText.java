package com.xiaolian.amigo.ui.widget.marqueeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;

import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class MarqueeText extends AppCompatTextView implements Runnable{

    public final static String TAG = MarqueeText.class.getSimpleName();

    private int currentScrollX = 0;// 初始滚动的位置
    private int firstScrollX = 0;
    private boolean isStop = false;
    private int textWidth;
    private int mWidth = 0; // 控件宽度
    //text每次滚动长度
    private int speed = 2;
    //text两次滚动时间间隔 ms
    private int delayed = 16;
    private int endX; // 滚动到哪个位置
    private boolean isFirstDraw = true; // 当首次或文本改变时重置
    //text一次滚动完成后，多长时间启动第二次滚动
    private int delayAgain =1000 ;

    /**
     * 滚动是否完成
     */
    public  boolean isEnd ;

    //   当前设置的文字
    private static CharSequence nowText ;

    private boolean canScrollForever = false  ;

    private ScrollFinishListener scrollFinishListener ;

    private List<String> list = new ArrayList<String>();

    public MarqueeText(Context context) {
        super(context);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScrollFinishListener(ScrollFinishListener scrollFinishListener) {
        this.scrollFinishListener = scrollFinishListener;
    }

    public void setCanScrollForever(boolean canScrollForever) {
        this.canScrollForever = canScrollForever;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw: " + getText() +"   " + isFirstDraw  );
        super.onDraw(canvas);
        if (isFirstDraw) {
            getTextWidth();
            firstScrollX = getScrollX(); // 起始位置不一定为0,改变内容后会变，需重新赋值
            currentScrollX = firstScrollX;
            mWidth = this.getWidth();
            endX = firstScrollX + textWidth + ScreenUtils.dpToPxInt(getContext() , 12);//字体滚完整个屏幕后再从头滚动
            if (endX <= mWidth){
                endX = 0 ;
            }else{
                endX -= mWidth ;
            }
            isFirstDraw = false;
        }
    }

    // 每次滚动几点

    public void setSpeed(int sp) {
        speed = sp;
    }
    // 滚动间隔时间,毫秒

    public void setDelayed(int delay) {
        delayed = delay;
    }
    /**
     * 设置滚动的list 内容
     * @param list
     */
    public void setList(List<String> list){
        if(list!=null&&list.size()!=0){
            this.list = list;
            setText(list.get(0));
        }

    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        nowText =  text ;
        isEnd = false ;
        super.setText(text, type);
    }

    /**
     * 获取文字宽度
     */
    private void getTextWidth() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        textWidth = (int) paint.measureText(str);
    }

    @Override
    public void run() {
        if (isStop) {
            return;
        }
        
        if (endX == 0){
            isEnd = true ;
            if (!canScrollForever){
                this.removeCallbacks(this);
                if (scrollFinishListener != null){
                    scrollFinishListener.scrollFinish();
                }
            }
            return ;
        }
        if (!canScrollForever){
            currentScrollX += speed;// 滚动速度,每次滚动几点
            if (currentScrollX >= endX) {
                isEnd = true ;
                scrollTo(currentScrollX , 0);
                    scrollTo(endX, 0);
                    isStop = true; // 停止滚动
                    this.removeCallbacks(this); // 清空队列
                    if (scrollFinishListener != null) {
                        scrollFinishListener.scrollFinish();
                    }
            } else {
                isEnd = false ;
                scrollTo(currentScrollX, 0);
                postDelayed(this, delayed);
            }
        }

    }

    /**
     * 释放view
     */
    public void onCancel(){
        isStop = true ;
        this.removeCallbacks(this);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        isFirstDraw = true; // 需重新设置参数
        Log.e(TAG, "onTextChanged: "  + getText() + getScrollX());
    }
    // 开始滚动
    public void startScroll() {
        isStop = false;
        this.removeCallbacks(this);
        postDelayed(this, 1000);
    }
    // 停止滚动
    public void stopScroll() {
        isStop = true;
    }
    // 从头开始滚动
    public void startFor0() {
        currentScrollX = 0;
        isFirstDraw = true ;
        startScroll();
    }

    public void startScrollForever(){
        setCanScrollForever(true);
        this.setMarqueeRepeatLimit(-1);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setHorizontallyScrolling(true);
        this.setSelected(true);
        this.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    }



    interface ScrollFinishListener{

        void scrollFinish();
    }




}
