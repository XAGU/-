package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;

import com.xiaolian.amigo.data.network.model.wallet.RechargeDenominations;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import lombok.Data;

/**
 * 充值Adaptor
 * <p>
 * Created by zcd on 9/20/17.
 */

public class RechargeAdaptor extends MultiItemTypeAdapter<RechargeAdaptor.RechargeWrapper> {

    public RechargeAdaptor(Context context, List<RechargeWrapper> datas) {
        super(context, datas);
    }

    @Data
    public static class RechargeWrapper {
        Long id;
        // 充值类型
        int type;
        // 主信息
        String main;
        // 附信息
        String sub;

        public RechargeWrapper(Integer type, String main, String sub) {
            this.type = type;
            this.main = main;
            this.sub = sub;
        }

        public RechargeWrapper(RechargeDenominations rechargeDenominations) {
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
                this.sub = "送" + rechargeDenominations.getValue() + "元红包";
            }
        }
    }
}
