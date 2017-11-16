package com.xiaolian.amigo.ui.main.adaptor;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.xiaolian.amigo.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.BannerType;
import com.xiaolian.amigo.data.network.model.dto.response.BannerDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Created by zcd on 10/11/17.
 */

public class HomeBannerDelegate implements ItemViewDelegate<HomeAdaptor.ItemWrapper> {
    private static final String TAG = HomeBannerDelegate.class.getSimpleName();
    private Context context;

    public HomeBannerDelegate(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_banner;
    }

    @Override
    public boolean isForViewType(HomeAdaptor.ItemWrapper item, int position) {
        return item.getType() == 2;
    }

    @Override
    public void convert(ViewHolder holder, HomeAdaptor.ItemWrapper itemWrapper, int position) {
        Banner banner = holder.getView(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        List<String> images = new ArrayList<>();
        for (BannerDTO dto : itemWrapper.getBanners()) {
            images.add(Constant.IMAGE_PREFIX + dto.getImage());
        }
        banner.setImages(images);
        banner.setOnBannerListener(position1 -> {
            Log.d(TAG, "onBannerClick");
            if (TextUtils.isEmpty(itemWrapper.getBanners().get(position1).getLink())) {
                return;
            }
            if (CommonUtil.equals(itemWrapper.getBanners().get(position1).getType(),BannerType.OUTSIDE.getType())) {
                context.startActivity(new Intent(context, WebActivity.class)
                        .putExtra(WebActivity.INTENT_KEY_URL, itemWrapper.getBanners().get(position1).getLink()));
            }
        });
        banner.start();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            if (path != null && path instanceof String) {
                Glide.with(context).load((String)path)
                        .asBitmap()
                        .placeholder(R.drawable.ic_picture_error)
                        .error(R.drawable.ic_picture_error)
                        .into(imageView);
            }
        }

    }
}
