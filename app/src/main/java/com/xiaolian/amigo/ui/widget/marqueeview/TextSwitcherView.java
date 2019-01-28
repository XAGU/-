package com.xiaolian.amigo.ui.widget.marqueeview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.xiaolian.amigo.R;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class TextSwitcherView extends TextSwitcher implements ViewSwitcher.ViewFactory ,MarqueeText.ScrollFinishListener {
    private static final String TAG = TextSwitcherView.class.getSimpleName();

    private ArrayList<String> info = new ArrayList<>();
    private int resIndex = 0 ;

    boolean isScroll = true  ;

    public TextSwitcherView(Context context) {
        this(context , null);
    }

    public TextSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setFactory(this::makeView);
        this.setInAnimation(getContext(), R.anim.anim_come_in);
        this.setOutAnimation(getContext() , R.anim.anim_get_out);
    }

    boolean isAnimation ;
    MarqueeText nextView ;

    public void updateText() {
        if (this.info != null && this.info.size() > 0 && isScroll) {
            nextView = (MarqueeText) getNextView();
            MarqueeText currentView = (MarqueeText) getCurrentView();
            if (nextView == null) return ;
            if (!currentView.getText().toString().isEmpty()) {
                /**
                 * 更新滚动条时，如果是一条消息，此时TextView是走马灯效果，不会调用onFinish接口，所以需要自己更新
                 */
                if ( !currentView.isCanScrollForever()&&!currentView.isEnd) return;
            }
            if (resIndex >= info.size()){
                resIndex = 0 ;
            }
            this.setText(info.get(resIndex++));
            if (getInAnimation() != null) {
                getInAnimation().setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        if (isAnimation) {
                            animation.cancel();
                        }
                        isAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        if (getCurrentView() != null && getCurrentView() instanceof MarqueeText) {
                            if (info.size() ==1){
                                Log.e(TAG, "onAnimationEnd: >>>>  forever  " );
                                 nextView.startScrollForever();
                            }else {
                                Log.e(TAG, "onAnimationEnd: >>>>  startFor0  " );
                                nextView.startFor0();
                            }
                        }
                        isAnimation = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                if (resIndex > this.info.size() - 1) {
                    resIndex = 0;
                }
            }
        }
    }

    public void destory(){
        int childCount = getChildCount();
        if (childCount > 0){
           View view =  getChildAt(childCount);
           if (view != null && view instanceof MarqueeText){
               ((MarqueeText)view).onCancel();
           }
        }
        removeAllViews();
    }

    /**
     * 停止滚动
     */
    public  void onStop(){
        View view = getCurrentView();
        if (view != null && view instanceof MarqueeText){
            ((MarqueeText) view).stopScroll();
        }
    }

    public void getResoure(ArrayList<String> info){
        this.info.clear();
        this.info.addAll(info);
        isScroll = true ;
        updateText();
    }

    public void setString(String info){
        MarqueeText marqueeText = (MarqueeText) makeView();
        marqueeText.setText(info);
        addView(marqueeText);
        marqueeText.startScrollForever();
    }

    @Override
    public View makeView() {
        MarqueeText textView = new MarqueeText(getContext());
        textView.setTextSize(12);
        textView.setTextColor(getContext().getResources().getColor(R.color.colorFullRed));
        textView.setSingleLine();
        textView.setScrollFinishListener(this);
        textView.setGravity(TEXT_ALIGNMENT_CENTER);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(MATCH_PARENT ,MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL ;
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    @Override
    public void scrollFinish() {
        updateText();
    }

    public void start() {
        View view = getCurrentView();
        if (view != null && view instanceof MarqueeText){
            ((MarqueeText) view).startScroll();
        }
    }
}
