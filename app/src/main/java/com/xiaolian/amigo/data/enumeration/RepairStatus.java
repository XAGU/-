package com.xiaolian.amigo.data.enumeration;

import com.xiaolian.amigo.R;

/**
 * 维修状态
 * <p>
 * Created by caidong on 2017/9/18.
 */
public enum RepairStatus {

    UNKNOWN(0, "未知状态") {
        @Override
        public int getTextCorlorRes() {
            return R.color.colorTextGray;
        }

        @Override
        public int getDotColorDrawableRes() {
            return R.drawable.dot_circle_grey;
        }

        @Override
        public String[] getNextOperations() {
            return new String[]{COMMON_PROBLEM, "联系客服"};
        }
    },
    AUDIT_PENDING(1, "正在处理") {
        @Override
        public int getTextCorlorRes() {
            return R.color.repair_todo;
        }

        @Override
        public int getDotColorDrawableRes() {
            return R.drawable.dot_circle_red;
        }

        @Override
        public String[] getNextOperations() {
            return new String[]{"提醒客服尽快处理", COMMON_PROBLEM};
        }
    }, REPAIR_PENDING(2, "等待维修") {
        @Override
        public int getTextCorlorRes() {
            return R.color.repair_waiting;
        }

        @Override
        public int getDotColorDrawableRes() {
            return R.drawable.dot_circle_yellow;
        }

        @Override
        public String[] getNextOperations() {
            return new String[]{"提醒客服尽快处理", COMMON_PROBLEM};
        }
    }, REPAIRING(3, "正在维修") {
        @Override
        public int getTextCorlorRes() {
            return R.color.repair_doing;
        }

        @Override
        public int getDotColorDrawableRes() {
            return R.drawable.dot_circle_yellow;
        }

        @Override
        public String[] getNextOperations() {
            return new String[]{"提醒客服尽快处理", COMMON_PROBLEM};
        }
    }, REPAIR_DONE(4, "维修完成") {
        @Override
        public int getTextCorlorRes() {
            return R.color.repair_done;
        }

        @Override
        public int getDotColorDrawableRes() {
            return R.drawable.dot_circle_green;
        }

        @Override
        public String[] getNextOperations() {
            return new String[]{"前往评价", COMMON_PROBLEM};
        }
    }, AUDIT_FAIL(5, "审核未通过") {
        @Override
        public int getTextCorlorRes() {
            return R.color.repair_todo;
        }

        @Override
        public int getDotColorDrawableRes() {
            return R.drawable.dot_circle_red;
        }

        @Override
        public String[] getNextOperations() {
            return new String[]{COMMON_PROBLEM, "联系客服"};
        }
    }, REPAIR_CANCEL(6, "取消报修") {
        @Override
        public int getTextCorlorRes() {
            return R.color.colorDark2;
        }

        @Override
        public int getDotColorDrawableRes() {
            return R.drawable.dot_circle_dark;
        }

        @Override
        public String[] getNextOperations() {
            return new String[]{COMMON_PROBLEM, "联系客服"};
        }
    };

    private static final String COMMON_PROBLEM = "常见问题";
    private int type;
    private String desc;

    RepairStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    // 获取文字颜色
    public abstract int getTextCorlorRes();

    // 获取提示圆点
    public abstract int getDotColorDrawableRes();

    // 获取下一步操作
    public abstract String[] getNextOperations();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static RepairStatus getStatus(int type) {
        for (RepairStatus status : RepairStatus.values()) {
            if (status.getType() == type) {
                return status;
            }
        }
        return UNKNOWN;
    }
}
