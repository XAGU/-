package com.xiaolian.amigo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaolian.amigo.R;

import java.util.Locale;

public class GildeUtils {


    /**
     * 加载本地图片
     * @param context
     * @param iv
     * @param path
     */
    public static void setPathImage(Context context , ImageView iv ,String path){
        Glide.with(context)
                .load(path)
                .asBitmap()
                .into(iv);
    }

    /**
     * 加载网络图片
     * @param context
     * @param view
     * @param url
     */
    public static void setImage(Context context , ImageView view ,String url){
        int imageSize = ScreenUtils.dpToPxInt(context, 57);
        Glide.with(context).load(Constant.IMAGE_PREFIX + url
                + String.format(Locale.getDefault(), Constant.OSS_IMAGE_RESIZE,
                imageSize))
                .asBitmap()
                .placeholder(R.drawable.ic_picture_error)
                .error(R.drawable.ic_picture_error)
                .into(view);
    }

    public static void setNoErrorImage(Context context , ImageView view ,String url  , float height , boolean isLast){
        Glide.with(context).load(Constant.IMAGE_PREFIX + url)
                .asBitmap()
                .skipMemoryCache(true)
                .dontAnimate()
//                .into(view);
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (view == null) return  ;
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                        float scale = height /resource.getHeight();
                        int vw = (int) (resource.getWidth() * scale);
                        params.height = (int) height;
                        params.width = vw;
                        if (!isLast)
                        params.setMarginEnd(ScreenUtils.dpToPxInt(context , 5));
                        else{
                            params.setMarginEnd(ScreenUtils.dpToPxInt(context , 15));
                        }
                        view.setLayoutParams(params);
                        view.setImageBitmap(resource);
//                        view.postInvalidate();

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
