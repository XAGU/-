package com.xiaolian.amigo.ui.repair.adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.GildeUtils;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 添加图片
 *
 * @author zcd
 * @date 17/11/15
 */

public class ImageAddAdapter2 extends CommonAdapter<ImageAddAdapter2.ImageItem> {
    private static final int DEFAULT_RES = R.drawable.image_add;

    private static final String TAG = ImageAddAdapter2.class.getSimpleName();
    private Context context;
    private int imageSize;
    private int viewWidth ;

    private boolean isNetImage ;
    public ImageAddAdapter2(Context context, int layoutId, List<ImageItem> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    public ImageAddAdapter2(Context context, int layoutId, List<ImageItem> datas , boolean isNetImage) {
        super(context, layoutId, datas);
        this.context = context;
        this.isNetImage = isNetImage ;
    }


    public void setViewWidth(int viewWidth){
        this.viewWidth  = viewWidth ;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder holder, ImageItem imageItem, int position) {
        if (viewWidth != 0){
            RelativeLayout relativeLayout = holder.getView(R.id.back_card_rl);
            ImageView ivTip =  holder.getView(R.id.id_card_back_tip);
            TextView tv = holder.getView(R.id.tv_back_card);
            if (relativeLayout.getWidth() != viewWidth) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.width = viewWidth;
                params.height = viewWidth;
                relativeLayout.setLayoutParams(params);
                    RelativeLayout.LayoutParams paramsBack = (RelativeLayout.LayoutParams) ivTip.getLayoutParams();
                    float imageBackWidth = viewWidth - ScreenUtils.dpToPx(context ,11) * 2;
                    Bitmap bitmapBack = BitmapFactory.decodeResource(context.getResources() ,R.drawable.id_card_back);
                    float scaleBack = imageBackWidth  /bitmapBack.getWidth();
                    paramsBack.width = (int) (bitmapBack.getWidth() * scaleBack);
                    paramsBack.height = (int) (bitmapBack.getHeight() * scaleBack);
                    paramsBack.setMargins(0 ,(viewWidth - paramsBack.height - ScreenUtils.dpToPxInt(context ,15)) / 8 * 5
                            , 0 , 0);
                    ivTip.setLayoutParams(paramsBack);
                }
            }
        if (TextUtils.isEmpty(imageItem.getImageUrl()) && (imageItem.getImageBase64Byte() == null
                  || imageItem.getImageBase64Byte().length == 0)) {
            ((ImageView) holder.getView(R.id.iv_back_card)).setImageDrawable(null);
//            ((ImageView) holder.getView(R.id.iv_image)).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//            holder.setImageResource(R.id.iv_image, DEFAULT_RES);
        } else {
            if (isNetImage){
                GildeUtils.setImage(context ,((ImageView)holder.getView(R.id.iv_back_card)) ,imageItem.getImageUrl());
                ((ImageView) holder.getView(R.id.iv_back_card)).setScaleType(ImageView.ScaleType.CENTER_CROP);
            }else {
                if (!TextUtils.isEmpty(imageItem.getImageUrl())) {

                    if (!imageItem.imageBase64Url) {
                        Glide.with(context).load(imageItem.getImageUrl())
                                .asBitmap()
                                .into((ImageView) holder.getView(R.id.iv_back_card));
                        ((ImageView) holder.getView(R.id.iv_back_card)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }else{
                        Glide.with(context).load(generateImage(Base64.decode(imageItem.imageUrl , Base64.DEFAULT)))
                                .asBitmap()
                                .into((ImageView) holder.getView(R.id.iv_back_card));
                        ((ImageView) holder.getView(R.id.iv_back_card)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }

                if (imageItem.getImageBase64Byte() != null && imageItem.getImageBase64Byte().length > 0){
                    Glide.with(context).load(generateImage(imageItem.getImageBase64Byte()))
                            .asBitmap()
                            .into((ImageView) holder.getView(R.id.iv_back_card));
                    ((ImageView) holder.getView(R.id.iv_back_card)).setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    try{
//                      Bitmap  bitmap = BitmapFactory.decodeByteArray(generateImage(imageItem.getImageBase64Byte()), 0, imageItem.getImageBase64Byte().length);
//                        ((ImageView) holder.getView(R.id.iv_image)).setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        holder.setImageBitmap(R.id.iv_image ,bitmap);
//                    }catch (Exception e){
//                        Log.wtf(TAG ,e.getMessage());
//                    }

                }
            }
        }
    }

    @Data
    public static class ImageItem {
        private String imageUrl;

        private byte[] imageBase64Byte ;


        private boolean imageBase64Url ;

        public ImageItem(byte[] imageBase64Byte){
            this.imageBase64Byte = imageBase64Byte ;
        }

        public ImageItem() {
        }

        public ImageItem(String imageUrl  ) {
            this.imageUrl = imageUrl;
            this.imageBase64Url  = false  ;
        }

        public ImageItem(String imageUrl , boolean imageBase64Url){
            this.imageUrl = imageUrl ;
            this.imageBase64Url = imageBase64Url ;
        }
    }


    //base64字符串转化成图片
    public  byte[] generateImage(byte[] b)
    {
        try
        {
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }

            return b ;
        }
        catch (Exception e)
        {
            return b;
        }
    }
}
