package com.xiaolian.amigo.ui.widget.marqueeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import com.xiaolian.amigo.R;

import java.util.ArrayList;

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
    MarqueeText currentView ;

    public void updateText() {
        if (this.info != null && this.info.size() > 0 && isScroll) {
            currentView = (MarqueeText) getCurrentView();
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
                                ((MarqueeText) getCurrentView()).startScrollForever();
                            }else {
                                ((MarqueeText) getCurrentView()).startFor0();
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

        this.info = info ;
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
