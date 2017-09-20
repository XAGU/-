package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;

import com.xiaolian.amigo.data.network.model.wallet.RechargeDenominations;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * 充值Adaptor
 * <p>
 * Created by zcd on 9/20/17.
 */

public class RechargeAdaptor extends MultiItemTypeAdapter<RechargeAdaptor.RechargeWapper> {

    public RechargeAdaptor(Context context, List<RechargeWapper> datas) {
        super(context, datas);
    }

    public static class RechargeWapper {
        Long id;
        // 充值类型
        int type;
        // 主信息
        String main;
        // 附信息
        String sub;

        public RechargeWapper(Integer type, String main, String sub) {
            this.type = type;
            this.main = main;
            this.sub = sub;
        }

        public RechargeWapper(RechargeDenominations rechargeDenominations) {
            if (rechargeDenominations.getId() != null) {
                this.id = rechargeDenominations.getId();
            }
            if (rechargeDenominations.getActivityType() != null) {
                this.type = rechargeDenominations.getActivityType();
            }
            this.main = rechargeDenominations.getAmount() + "元";
            if (this.type == 1) {
                this.sub = "售价" + rechargeDenominations.getValue() + "元";
            } else if (this.type == 2) {
                this.sub = "送" + rechargeDenominations.getValue() + "红包";
            }
        }
    }
}
