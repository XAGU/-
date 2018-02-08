package com.xiaolian.amigo.data.enumeration;

/**
 * 提现方式
 *
 * @author zcd
 * @date 17/10/26
 */

public enum WithdrawWay {
    ALIPAY(1, "支付宝");
    private int type;
    private String desc;

    WithdrawWay(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
