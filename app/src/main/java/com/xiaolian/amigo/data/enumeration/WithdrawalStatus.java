package com.xiaolian.amigo.data.enumeration;

import com.xiaolian.amigo.R;

/**
 * 充值提现状态
 * <p>
 * Created by zcd on 10/24/17.
 */

public enum WithdrawalStatus {
    KNOWN(0, "未知状态") {
        @Override
        public int getColorRes() {
            return R.color.colorFullRed;
        }
    },
    AUDIT_PENDING(1, "等待审核") {
        @Override
        public int getColorRes() {
            return R.color.colorFullRed;
        }
    },
    AUDIT_FAIL(2, "审核失败") {
        @Override
        public int getColorRes() {
            return R.color.colorFullRed;
        }
    },
    THIRD_PENDING(3, "等待支付确认") {
        @Override
        public int getColorRes() {
            return R.color.colorFullRed;
        }
    },
    WITHDRAWAL_SUCCESS(4, "提现成功") {
        @Override
        public int getColorRes() {
            return R.color.device_dispenser;
        }
    },
    WITHDRAWAL_FAIL(5, "提现失败") {
        @Override
        public int getColorRes() {
            return R.color.colorFullRed;
        }
    };
    private int type;
    private String desc;

    WithdrawalStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public abstract int getColorRes();

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
