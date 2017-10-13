package com.xiaolian.amigo.data.enumeration;

/**
 * Created by caidong on 2017/10/12.
 */
public enum TradeStep {

    PAY(1, "支付页"), SETTLE(2, "结算页");

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
