package com.xiaolian.amigo.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.RxHelper;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

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

    private void init(){
        this.setFactory(this::makeView);
        this.setInAnimation(getContext() , R.anim.anim_come_in);
        this.setOutAnimation(getContext() , R.anim.anim_get_out);
    }

    boolean isAnimation ;

    public void updateText(){

        if (this.info != null && this.info.size()>0 && isScroll) {
            setText(info.get(resIndex++));
            RxHelper.delay(1, aLong -> {
                if (getCurrentView() != null && getCurrentView() instanceof  MarqueeText) {
                    ((MarqueeText) getCurrentView()).startFor0();
                }
            });
        }
    }
    public void updateTextSwitcher() {
        if (this.info != null && this.info.size()>0 && isScroll) {
            showNext();
            if(getInAnimation() != null){
                getInAnimation().setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if (isAnimation){
                            animation.cancel();
                        }
                        ((MarqueeText) getCurrentView()).setText(info.get(resIndex++));
                        isAnimation = true ;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
//                        setText(info.get(resIndex++));
                        RxHelper.delay(1, aLong -> {
                            if (getCurrentView() != null && getCurrentView() instanceof  MarqueeText) {
                                ((MarqueeText) getCurrentView()).startFor0();
                            }
                        });

                        if (resIndex > info.size()-1) {
                            resIndex = 0;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }



        }

    }

    /**
     * 停止滚动
     */
    public  void onStop(){
        isScroll = false ;
    }

    public void getResoure(ArrayList<String> info){
        this.info = info ;
        isScroll = true ;
//        updateTextSwitcher();
        updateText();
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
        updateTextSwitcher();
    }

    public void start() {
        View view = getCurrentView();
        if (view != null && view instanceof MarqueeText){
            ((MarqueeText) view).startScroll();
        }
    }
}
