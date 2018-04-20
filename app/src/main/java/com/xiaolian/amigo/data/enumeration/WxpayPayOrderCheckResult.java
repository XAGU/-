package com.xiaolian.amigo.data.enumeration;

/**
 * 微信支付结果
 *
 * @author zcd
 * @date 17/10/25
 */

public enum WxpayPayOrderCheckResult {
    SUCCESS(1, "订单支付成功"),
    REFUND(2, "订单转入退款"),
    NOTPAY(3, "订单未支付"),
    CLOSED(4, "订单已关闭"),
    // 刷卡支付
    REVOKED(5, "订单已撤销"),
    USERPAYING(6, "订单支付中"),
    // 其他原因，如银行返回失败
    PAYERROR(7, "订单支付失败"),
    // 服务器内部状态
    CANCEL(8, "订单已取消"),
    SIGN_INVALID(9, "签名验证失败"),
    DATA_INVALID(10, "数据不合法"),
    INNER_CHECK_ERROR(11, "内部校验错误");

    private int type;
    private String desc;

    WxpayPayOrderCheckResult(int type, String desc) {
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
