package com.xiaolian.amigo.data.enumeration;

/**
 * 投诉类型
 * {1: '热水器', 2: '饮水机', 3: '充值', 4: '提现'}
 * <p>
 * Created by zcd on 17/11/2.
 */

public enum  ComplaintType {
    UNKNOWN(0, "未知类型"),
    HEATER(1, "热水器"),
    DISPENSER(2, "饮水机"),
    RECHARGE(3, "充值"),
    WITHDRAW(4, "提现")
    ;
    int type;
    String desc;

    ComplaintType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static ComplaintType getComplaintTypeByDeviceType(Device type) {
        switch (type) {
            case HEATER:
                return HEATER;
            case DISPENSER:
                return DISPENSER;
        }
        return UNKNOWN;
    }

    public static ComplaintType getComplaintTypeByWithdrawType(WithdrawOperationType type) {
        switch (type) {
            case WITHDRAW:
                return WITHDRAW;
            case RECHARGE:
                return RECHARGE;
        }
        return UNKNOWN;
    }
}
