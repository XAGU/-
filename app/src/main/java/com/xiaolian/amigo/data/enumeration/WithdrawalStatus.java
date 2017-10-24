package com.xiaolian.amigo.data.enumeration;

/**
 * 充值提现状态
 * <p>
 * Created by zcd on 10/24/17.
 */

public enum WithdrawalStatus {
    KNOWN(0, "未知状态"),
    AUDIT_PENDING(1, "等待审核"),
    AUDIT_PASSED(2, "审核通过"),
    WITHDRAWAL_SUCCESS(3, "提现充值成功"),
    AUDIT_FAIL(4, "审核失败"),
    WITHDRAWAL_FAIL(5, "提现充值失败");
    private int type;
    private String desc;

    WithdrawalStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static WithdrawalStatus getWithdrawalStatus(int type) {
        for (WithdrawalStatus status : WithdrawalStatus.values()) {
            if (status.getType() == type) {
                return status;
            }
        }
        return KNOWN;
    }
}
