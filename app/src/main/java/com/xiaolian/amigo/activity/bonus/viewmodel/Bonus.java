package com.xiaolian.amigo.activity.bonus.viewmodel;

/**
 * Created by adamzfc on 9/12/17.
 */

public class Bonus {
    // 红包类型
    Integer type;
    // 红包金额
    Integer amount;
    // 到期时间
    String timeEnd;
    // 描述信息
    String desc;
    // 剩余时间
    Integer timeLeft;

    public Bonus(Integer type, Integer amount, String timeEnd, String desc, Integer timeLeft) {
        this.type = type;
        this.amount = amount;
        this.timeEnd = timeEnd;
        this.desc = desc;
        this.timeLeft = timeLeft;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }
}
