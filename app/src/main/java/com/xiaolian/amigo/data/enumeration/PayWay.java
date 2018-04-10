package com.xiaolian.amigo.data.enumeration;

import com.xiaolian.amigo.R;

/**
 * 支付方式
 *
 * @author zcd
 * @date 17/10/25
 */

public enum PayWay {
    KNOWN(0, "未知方式") {
        @Override
        public int getDrawableRes() {
            return 0;
        }
    },
    ALIAPY(1, "支付宝") {
        @Override
        public int getDrawableRes() {
            return R.drawable.ic_alipay;
        }
    },
    WECHAT(2, "微信") {
        @Override
        public int getDrawableRes() {
            return R.drawable.ic_wxpay;
        }
    };

    PayWay(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    private int type;
    private String desc;

    public abstract int getDrawableRes();

    public static PayWay getPayWay(int type) {
        for (PayWay payWay : PayWay.values()) {
            if (payWay.getType() == type) {
                return payWay;
            }
        }
        return KNOWN;
    }
}
