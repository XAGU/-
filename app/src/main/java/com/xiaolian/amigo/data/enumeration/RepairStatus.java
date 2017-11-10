package com.xiaolian.amigo.data.enumeration;

import com.xiaolian.amigo.R;

/**
 * 维修状态
 * <p>
 * Created by caidong on 2017/9/18.
 */
public enum RepairStatus {

    AUDIT_PENDING(1, "等待审核") {
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
            return new String[]{"提醒客服尽快处理", "常见问题"};
        }
    }, REPAIR_PENDING(2, "等待维修") {
        @Override
        public int getTextCorlorRes() {
            return R.color.repair_waiting;
        }

        @Override
        public int getDotColorDrawableRes() {
            return R.drawable.dot_circle_red;
        }

        @Override
        public String[] getNextOperations() {
            return new String[]{"提醒客服尽快处理", "常见问题"};
        }
    }, REPAIRING(3, "正在维修") {
        @Override
        public int getTextCorlorRes() {
            return R.color.repair_doing;
        }

        @Override
        public int getDotColorDrawableRes() {
            return R.drawable.dot_circle_blue;
        }

        @Override
        public String[] getNextOperations() {
            return new String[]{"提醒客服尽快处理", "常见问题"};
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
            return new String[]{"前往评价", "常见问题"};
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
            return new String[]{"常见问题", "联系客服"};
        }
    };

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
        return null;
    }
}
