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

        @Override
        public String[] getNextOperations() {
            return new String[] {"常见问题", "我要投诉"};
        }
    },
    AUDIT_PENDING(1, "等待审核") {
        @Override
        public int getColorRes() {
            return R.color.colorFullRed;
        }

        @Override
        public String[] getNextOperations() {
            return new String[] {"提醒客服尽快处理", "常见问题"};
        }
    },
    AUDIT_FAIL(2, "审核失败") {
        @Override
        public int getColorRes() {
            return R.color.colorFullRed;
        }

        @Override
        public String[] getNextOperations() {
            return new String[] {"常见问题", "联系客服"};
        }
    },
    THIRD_PENDING(3, "等待支付确认") {
        @Override
        public int getColorRes() {
            return R.color.colorFullRed;
        }

        @Override
        public String[] getNextOperations() {
            return new String[] {"常见问题", "我要投诉"};
        }
    },
    WITHDRAWAL_SUCCESS(4, "提现成功") {
        @Override
        public int getColorRes() {
            return R.color.device_dispenser;
        }

        @Override
        public String[] getNextOperations() {
            return new String[] {"常见问题", "我要投诉"};
        }
    },
    WITHDRAWAL_FAIL(5, "提现失败") {
        @Override
        public int getColorRes() {
            return R.color.colorFullRed;
        }

        @Override
        public String[] getNextOperations() {
            return new String[] {"常见问题", "联系客服"};
        }
    },
    WITHDRAWAL_CANCEL(6, "取消提现") {
        @Override
        public int getColorRes() {
            return R.color.colorTextGray;
        }

        @Override
        public String[] getNextOperations() {
            return new String[] {"常见问题", "联系客服"};
        }
    };
    private int type;
    private String desc;

    WithdrawalStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public abstract int getColorRes();

    // 获取下一步操作
    public abstract String[] getNextOperations();

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
