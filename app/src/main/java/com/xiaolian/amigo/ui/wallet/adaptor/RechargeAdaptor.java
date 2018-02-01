package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;

import com.xiaolian.amigo.data.network.model.wallet.RechargeDenominations;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import lombok.Data;

/**
 * 充值Adaptor
 *
 * @author zcd
 * @date 17/9/20
 */

public class RechargeAdaptor extends MultiItemTypeAdapter<RechargeAdaptor.RechargeWrapper> {

    public RechargeAdaptor(Context context, List<RechargeWrapper> datas) {
        super(context, datas);
    }

    @Data
    public static class RechargeWrapper {
        Long id;
        /**
         * 充值类型
         */
        int type;
        Double amount;
        /**
         * 主信息
         */
        String main;
        /**
         * 附信息
         */
        String sub;
        boolean isSelected = false;
        private DecimalFormat df = new DecimalFormat("###.##");

        public RechargeWrapper(Integer type, String main, String sub) {
            this.type = type;
            this.main = main;
            this.sub = sub;
        }

        public RechargeWrapper(RechargeDenominations rechargeDenominations, boolean isSelected) {
            this(rechargeDenominations);
            this.isSelected = isSelected;
        }

        public RechargeWrapper(RechargeDenominations rechargeDenominations) {
            this.amount = rechargeDenominations.getAmount();
            if (rechargeDenominations.getId() != null) {
                this.id = rechargeDenominations.getId();
            }
            if (rechargeDenominations.getActivityType() != null) {
                this.type = rechargeDenominations.getActivityType();
            }
            this.main = String.format(Locale.getDefault(),
                    "%.0f元", rechargeDenominations.getAmount());
            if (this.type == 1) {
                this.sub = "售价" + df.format(rechargeDenominations.getValue()) + "元";
            } else if (this.type == 2) {
                this.sub = "送" + df.format(rechargeDenominations.getValue()) + "元代金券";
            }
        }
    }
}
