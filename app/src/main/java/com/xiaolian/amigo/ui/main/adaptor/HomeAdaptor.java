package com.xiaolian.amigo.ui.main.adaptor;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import lombok.Data;

/**
 * 首页adapter
 *
 * @author zcd
 * @date 17/10/10
 */

public class HomeAdaptor extends MultiItemTypeAdapter<HomeAdaptor.ItemWrapper> {
    public static final int NORMAL_TYPE = 1;
    public static final int BANNER_TYPE = 2;
    public static final int SMALL_TYPE = 3;

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
//        if (getDatas().size() % 2 == 0 && position == getDatas().size() - 2) {
//            return true;
//        }
        return getDatas().get(position).getType() == BANNER_TYPE;
    }

    @Data
    public static class ItemWrapper {
        public ItemWrapper(int type, List<BannerDTO> banners, String deviceName, String desc, int res, int smallRes) {

            this.type = type;
            this.banners = banners;
            this.deviceName = deviceName;
            this.desc = desc;
            this.res = res;
            this.smallRes = smallRes;
        }

        int type;
        List<BannerDTO> banners;
        String deviceName;
        String desc;
        int smallRes;
        int res;
        int prepaySize = 0;
        boolean active = true;
        boolean using = false;
        boolean existOrder = false ;  // 是否存在预约订单
        int status ;  //  预约状态   2 等待洗浴 5 预约超时 6正在洗浴

    }
}
