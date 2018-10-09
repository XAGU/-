package com.xiaolian.amigo.data.enumeration;

/**
 * 投诉类型
 * {1: '热水器', 2: '饮水机', 3: '充值', 4: '提现'}
 *
 * @author zcd
 * @date 17/11/2
 */

public enum ComplaintType {
    UNKNOWN(0, "未知类型"),
    HEATER(1, "热水器"),
    DISPENSER(2, "饮水机"),
    RECHARGE(3, "充值"),
    WITHDRAW(4, "提现"),
    DRYER(5, "吹风机"),
    WASHER(6, "洗衣机"),
    DRYER2(7,"烘干机");

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
            case DRYER:
                return DRYER;
            case WASHER:
                return WASHER;
            case DRYER2:
                return DRYER2;
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
