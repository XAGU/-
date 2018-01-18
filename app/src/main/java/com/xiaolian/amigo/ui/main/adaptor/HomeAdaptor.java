package com.xiaolian.amigo.ui.main.adaptor;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 10/10/17.
 */

public class HomeAdaptor extends MultiItemTypeAdapter<HomeAdaptor.ItemWrapper> {
    public HomeAdaptor(Context context, List<ItemWrapper> datas, GridLayoutManager gridLayoutManager) {
        super(context, datas);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isBannerSection(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
    }

    private boolean isBannerSection(int position) {
        return getDatas().get(position).getType() == 2;
    }

    @Data
    public static class ItemWrapper {
        public ItemWrapper(int type, List<BannerDTO> banners, String deviceName, String desc, String descColor, int res) {

            this.type = type;
            this.banners = banners;
            this.deviceName = deviceName;
            this.desc = desc;
            this.descColor = descColor;
            this.res = res;
        }

        int type;
        List<BannerDTO> banners;
        String deviceName;
        String desc;
        String descColor;
        int res;
        int prepaySize = 0;
        boolean active = true;
        boolean using = false;
    }
}
