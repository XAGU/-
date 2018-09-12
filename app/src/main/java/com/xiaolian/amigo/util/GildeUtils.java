package com.xiaolian.amigo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
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

    public static void setNoErrorImage(Context context , ImageView view ,String url){
        Glide.with(context).load(Constant.IMAGE_PREFIX + url)
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        return false;
                    }
                })
                .into(view);
    }
}
