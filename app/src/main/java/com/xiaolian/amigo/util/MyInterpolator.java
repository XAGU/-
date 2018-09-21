package com.xiaolian.amigo.util;

import android.view.animation.DecelerateInterpolator;

public class MyInterpolator extends DecelerateInterpolator {
    private static final String TAG = MyInterpolator.class.getSimpleName();
    @Override
    public float getInterpolation(float input) {
        Log.d(TAG ,super.getInterpolation(input) +"");
        if (super.getInterpolation(input) > 0.85f){
            return 0.85f ;
        }else {
            return super.getInterpolation(input);
        }

    }
}