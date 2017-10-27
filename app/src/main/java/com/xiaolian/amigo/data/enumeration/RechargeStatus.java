package com.xiaolian.amigo.data.enumeration;

import com.xiaolian.amigo.R;

/**
 * 提现状态
 * <p>
 * Created by zcd on 10/27/17.
 */

public enum  RechargeStatus {
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
    AUDIT_PASSED(2, "审核通过") {
        @Override
        public int getColorRes() {
            return R.color.device_dispenser;
        }
    },
    AUDIT_FAIL(3, "审核失败") {
        @Override
        public int getColorRes() {
            return R.color.colorFullRed;
        }
    },
    THIRD_PENDING(4, "等待支付确认") {
        @Override
        public int getColorRes() {
            return R.color.colorFullRed;
        }
    },
    WITHDRAWAL_SUCCESS(5, "充值成功") {
        @Override
        public int getColorRes() {
            return R.color.device_dispenser;
        }
    },
    WITHDRAWAL_FAIL(6, "充值失败") {
        @Override
        public int getColorRes() {
            return R.color.colorFullRed;
        }
    };
    private int type;
    private String desc;

    RechargeStatus(int type, String desc) {
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

    public static RechargeStatus getRechargeStatus(int type) {
        for (RechargeStatus status : RechargeStatus.values()) {
            if (status.getType() == type) {
                return status;
            }
        }
        return KNOWN;
    }
}
