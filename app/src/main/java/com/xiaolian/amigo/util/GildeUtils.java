package com.xiaolian.amigo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.xiaolian.amigo.R;

public class GildeUtils {

    public static void setImage(Context context , ImageView view ,String url){
        Glide.with(context).load(Constant.IMAGE_PREFIX + url)
                .asBitmap()
                .placeholder(R.drawable.ic_picture_error)
                .error(R.drawable.ic_picture_error)
                .into(view);
    }

    public static void setNoErrorImage(Context context , ImageView view ,String url  , float height){

//        Glide.with(context).load(Constant.IMAGE_PREFIX + url)
//                .into(view);
        Glide.with(context).load(Constant.IMAGE_PREFIX + url)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        if (view == null) return  ;
                        ViewGroup.LayoutParams params = view.getLayoutParams();
                        float scale = height /resource.getMinimumHeight();
                        int vw = (int) (resource.getMinimumWidth() * scale);
                        params.height = (int) height;
                        params.width = vw;
//                        view.setLayoutParams(params);
                        view.setImageDrawable(resource);
                    }
                });
    }
}
