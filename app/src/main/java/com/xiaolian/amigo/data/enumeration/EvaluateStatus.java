package com.xiaolian.amigo.data.enumeration;

import com.xiaolian.amigo.R;

/**
 * 评价状态
 * <p>
 * Created by caidong on 2017/9/18.
 */
public enum EvaluateStatus {

    EVALUATE_PENDING(1, "未评价") {
        @Override
        public int getBackGroundRes() {
            return R.drawable.button_evaluate_todo;
        }

        @Override
        public int getTextColor() {
            return R.color.evaluate_todo;
        }
    }, EVALUATE_DONE(2, "已评价") {
        @Override
        public int getBackGroundRes() {
            return R.drawable.button_evaluate_done;
        }

        @Override
        public int getTextColor() {
            return R.color.evaluate_done;
        }
    };

    private int type;
    private String desc;

    EvaluateStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    // 获取背景图片
    public abstract int getBackGroundRes();

    // 获取字体颜色
    public abstract int getTextColor();

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

    public static EvaluateStatus getStatus(int type) {
        for (EvaluateStatus status : EvaluateStatus.values()) {
            if (status.getType() == type) {
                return status;
            }
        }
        return null;
    }
}
