package com.xiaolian.amigo.data.enumeration;

/**
 * 支付宝支付结果
 * <p>
 * Created by zcd on 10/25/17.
 */

public enum AlipayPayOrderCheckResult {
    SUCCESS(1, "订单支付成功"),
    FAIL(2, "订单支付失败"),
    UNKNOWN(3, "订单状态未知"),
    PAYING(4, "订单支付中"),
    SIGN_INVALID(5, "签名验证失败"),
    DATA_INVALID(6, "数据不合法"),
    INNER_CHECK_ERROR(7, "内部校验错误");
    private int type;
    private String desc;

    AlipayPayOrderCheckResult(int type, String desc) {
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
