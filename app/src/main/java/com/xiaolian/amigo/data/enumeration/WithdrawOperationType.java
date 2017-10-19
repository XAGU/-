package com.xiaolian.amigo.data.enumeration;

/**
 * 充值提现类型
 * <p>
 * Created by zcd on 10/19/17.
 */

public enum  WithdrawOperationType {
    UNKNOWN(-1, "未知操作"),
    RECHARGE(1, "充值"),
    WITHDRAW(2, "提现")
    ;
    private int type;
    private String desc;

    WithdrawOperationType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static WithdrawOperationType getOperationType(int type) {
        for (WithdrawOperationType operationType : WithdrawOperationType.values()) {
            if (operationType.getType() == type) {
                return operationType;
            }
        }
        return UNKNOWN;
    }
}
