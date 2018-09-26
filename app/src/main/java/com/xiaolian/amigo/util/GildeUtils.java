package com.xiaolian.amigo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaolian.amigo.R;

public class GildeUtils {

    public static void setImage(Context context , ImageView view ,String url){
        Glide.with(context).load(Constant.IMAGE_PREFIX + url)
                .asBitmap()
                .placeholder(R.drawable.ic_picture_error)
                .error(R.drawable.ic_picture_error)
                .skipMemoryCache(true)
                .into(view);
    }

    public static void setNoErrorImage(Context context , ImageView view ,String url  , float height){
        Glide.with(context).load(Constant.IMAGE_PREFIX + url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (view == null) return  ;
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        float scale = height /resource.getHeight();
                        int vw = (int) (resource.getWidth() * scale);
                        params.height = (int) height;
                        params.width = vw;
                        params.setMarginEnd(ScreenUtils.dpToPxInt(context , 5));
                        view.setLayoutParams(params);
                        view.setImageBitmap(resource);
//                        view.postInvalidate();
                        Log.d("Glide" , resource.toString());
                    }
                });
//                .into(view);
//                .diskCacheStrategy(true)
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        if (view == null) return  ;
//                        ViewGroup.LayoutParams params = view.getLayoutParams();
//                        float scale = height /resource.getMinimumHeight();
//                        int vw = (int) (resource.getMinimumWidth() * scale);
//                        params.height = (int) height;
//                        params.width = vw;
//                        view.setLayoutParams(params);
//                        view.setImageDrawable(resource);
//                        Log.d("Glide" , resource.toString());
//                    }
//
//                    @Override
//                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                        super.onLoadFailed(e, errorDrawable);
//                        Log.d("Glide" ,"error:   "  + e.getMessage());
//                    }
//                });
    }
}
