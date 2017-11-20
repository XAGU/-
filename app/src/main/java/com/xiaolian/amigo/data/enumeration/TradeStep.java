package com.xiaolian.amigo.data.enumeration;

/**
 * trade step
 * Created by caidong on 2017/10/12.
 */
public enum TradeStep {

    // 结账中状态是为了防止结账后设备断开蓝牙导致app端无法接受到服务器的返回数据
    PAY(1, "支付页"), SETTLE(2, "结算页"), CLOSE_VALVE(3, "结账中");

    private int step;
    private String desc;

    TradeStep(int step, String desc) {
        this.step = step;
        this.desc = desc;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
