package com.xiaolian.amigo.ui.repair.adaptor;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;

import lombok.Data;

/**
 * 添加图片
 *
 * @author zcd
 * @date 17/11/15
 */

public class ImageAddAdapter extends CommonAdapter<ImageAddAdapter.ImageItem> {
    private static final int DEFAULT_RES = R.drawable.image_add;
    private Context context;
    private int imageSize;
    private int viewWidth ;
    public ImageAddAdapter(Context context, int layoutId, List<ImageItem> datas) {
        super(context, layoutId, datas);
        this.context = context;
        this.imageSize = ScreenUtils.dpToPxInt(context, 57);
    }


    public void setViewWidth(int viewWidth){
        this.viewWidth  = viewWidth ;
        notifyDataSetChanged();
    }


    @Override
    protected void convert(ViewHolder holder, ImageItem imageItem, int position) {
        if (viewWidth != 0){
            ImageView imageView = holder.getView(R.id.iv_image);
            if (imageView.getWidth() != viewWidth) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.width = viewWidth;
                params.height = viewWidth;
                imageView.setLayoutParams(params);
            }
        }
        if (TextUtils.isEmpty(imageItem.getImageUrl())) {
//            Glide.with(context).load(defaultRes)
//                    .asBitmap()
//                    .placeholder(R.drawable.ic_picture_error)
//                    .error(R.drawable.ic_picture_error)
////                .skipMemoryCache(true)
////                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into((ImageView)holder.getView(R.id.iv_image));

            ((ImageView) holder.getView(R.id.iv_image)).setImageDrawable(null);
            ((ImageView) holder.getView(R.id.iv_image)).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.setImageResource(R.id.iv_image, DEFAULT_RES);
        } else {
            Glide.with(context).load(Constant.IMAGE_PREFIX + imageItem.getImageUrl()
                    + String.format(Locale.getDefault(), Constant.OSS_IMAGE_RESIZE,
                    imageSize))
                    .asBitmap()
                .skipMemoryCache(true)
                    .error(R.drawable.ic_picture_error)
                    .placeholder(R.drawable.ic_picture_error)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into((ImageView) holder.getView(R.id.iv_image));
            ((ImageView) holder.getView(R.id.iv_image)).setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Data
    public static class ImageItem {
        private String imageUrl;

        public ImageItem() {
        }

        public ImageItem(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
