package com.xiaolian.amigo.data.enumeration;

import com.xiaolian.amigo.R;

/**
 * 支付方式
 * <p>
 * Created by zcd on 10/25/17.
 */

public enum PayWay {
    ALIAPY(1, "支付宝") {
        @Override
        public int getDrawableRes() {
            return R.drawable.ic_alipay;
        }
    },
    WECHAT(2, "微信") {
        @Override
        public int getDrawableRes() {
            return 0;
        }
    };

    PayWay(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int type;
    private String desc;
    public abstract int getDrawableRes();
}
