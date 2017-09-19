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
        public int getCorlorRes() {
            return R.color.repair_todo;
        }
    }, REPAIR_PENDING(2, "等待维修") {
        @Override
        public int getCorlorRes() {
            return R.color.repair_todo;
        }
    }, REPAIRING(3, "正在维修") {
        @Override
        public int getCorlorRes() {
            return R.color.repair_doing;
        }
    }, REPAIR_DONE(4, "维修完成") {
        @Override
        public int getCorlorRes() {
            return R.color.repair_done;
        }
    }, AUDIT_FAIL(5, "审核未通过") {
        @Override
        public int getCorlorRes() {
            return R.color.repair_todo;
        }
    };

    private int type;
    private String desc;

    RepairStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public abstract int getCorlorRes();

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
