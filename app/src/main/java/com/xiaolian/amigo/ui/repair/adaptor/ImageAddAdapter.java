package com.xiaolian.amigo.ui.repair.adaptor;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

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
 * <p>
 * Created by zcd on 17/11/15.
 */

public class ImageAddAdapter extends CommonAdapter<ImageAddAdapter.ImageItem> {
    public static final int defaultRes = R.drawable.device_picture_add;
    private Context context;
    private int imageSize;
    public ImageAddAdapter(Context context, int layoutId, List<ImageItem> datas) {
        super(context, layoutId, datas);
        this.context = context;
        this.imageSize = ScreenUtils.dpToPxInt(context, 57);
    }

    @Override
    protected void convert(ViewHolder holder, ImageItem imageItem, int position) {
        if (TextUtils.isEmpty(imageItem.getImageUrl())) {
//            Glide.with(context).load(defaultRes)
//                    .asBitmap()
//                    .placeholder(R.drawable.ic_picture_error)
//                    .error(R.drawable.ic_picture_error)
////                .skipMemoryCache(true)
////                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into((ImageView)holder.getView(R.id.iv_image));

            ((ImageView)holder.getView(R.id.iv_image)).setImageDrawable(null);
            ((ImageView)holder.getView(R.id.iv_image)).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.setImageResource(R.id.iv_image, defaultRes);
        } else {
            Glide.with(context).load(Constant.IMAGE_PREFIX + imageItem.getImageUrl()
                    + String.format(Locale.getDefault(), Constant.OSS_IMAGE_RESIZE,
                    imageSize, imageSize))
//                    .asBitmap()
                    .placeholder(R.drawable.ic_picture_error)
                    .error(R.drawable.ic_picture_error)
//                .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into((ImageView)holder.getView(R.id.iv_image));
            ((ImageView)holder.getView(R.id.iv_image)).setScaleType(ImageView.ScaleType.CENTER_CROP);
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
