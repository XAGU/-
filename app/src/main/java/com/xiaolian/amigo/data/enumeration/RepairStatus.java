package com.xiaolian.amigo.data.enumeration;

import com.xiaolian.amigo.R;

/**
 * 维修状态
 * <p>
 * Created by caidong on 2017/9/18.
 */
public enum RepairStatus {

    TO_AUDIT(1, "待审核") {
        @Override
        public int getCorlorRes() {
            return R.color.repair_todo;
        }
    }, TO_ASSIGN(2, "待指派") {
        @Override
        public int getCorlorRes() {
            return R.color.repair_todo;
        }
    }, REPAIRING(3, "维修中") {
        @Override
        public int getCorlorRes() {
            return R.color.repair_doing;
        }
    }, REPAIR_DONE(4, "已结束") {
        @Override
        public int getCorlorRes() {
            return R.color.repair_done;
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
