package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.BannerType;
import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.main.adaptor.HomeAdaptor;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 失物招领详情内容
 *
 * @author zcd
 * @date 18/5/14
 */

public class LostAndFoundDetailContentDelegate
        implements ItemViewDelegate<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> {
    private static final String TAG = LostAndFoundDetailContentDelegate.class.getSimpleName();
    private Context context;

    public LostAndFoundDetailContentDelegate(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_lost_and_found_detail_top;
    }

    @Override
    public boolean isForViewType(LostAndFoundDetailAdapter.LostAndFoundDetailWrapper item, int position) {
        if (item == null) {
            return false;
        }
        return ObjectsCompat.equals(item.getItemType(),
                LostAndFoundDetailAdapter.LostAndFoundDetailItemType.CONTENT);
    }

    @Override
    public void convert(ViewHolder holder, LostAndFoundDetailAdapter.LostAndFoundDetailWrapper lostAndFoundDetailWrapper, int position) {
        holder.setText(R.id.tv_content_title, lostAndFoundDetailWrapper.getContentTitle());
        holder.setText(R.id.tv_content_desc, lostAndFoundDetailWrapper.getContent());
        setStat(holder.getView(R.id.tv_stat), lostAndFoundDetailWrapper.getViewCount(), lostAndFoundDetailWrapper.getCommentCount());
    }

    private void setStat(TextView textView, Integer viewCount, Integer commentCount) {
        textView.setText(String.format(Locale.getDefault(), "%d查看·%d回复", viewCount, commentCount));
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            if (path != null && path instanceof String) {
                Glide.with(context).load((String) path)
                        .asBitmap()
                        .placeholder(R.drawable.ic_picture_error)
                        .error(R.drawable.ic_picture_error)
                        .into(imageView);
            }
        }

    }
}
