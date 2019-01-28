package com.xiaolian.amigo.data.enumeration;

import android.support.annotation.DrawableRes;

import com.xiaolian.amigo.R;

/**
 * 通知类型
 *
 * @author zcd
 * @date 17/9/22
 */

public enum Notice {
    UNKNOWN(0, "通知") {
        @Override
        public int getDrawableRes() {
            return 0;
        }
    },
    EMERGENCY(1, "紧急通知") {
        @Override
        public int getDrawableRes() {
            return R.drawable.ic_emergency_notice;
        }
    },
    CUSTOMER_SERVICE(3, "客服通知") {
        @Override
        public int getDrawableRes() {
            return R.drawable.ic_customer_service_notice;
        }
    },
    SYSTEM(2, "系统通知") {
        @Override
        public int getDrawableRes() {
            return R.drawable.ic_system_notice;
        }
    },ROLLING_NOTICE(5,"滚动公告"){
        @Override
        public int getDrawableRes() {
            return R.drawable.ic_rolling_notice;
        }
    };
    private int type;
    private String desc;

    Notice(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

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


    public abstract @DrawableRes
    int getDrawableRes();

    public static Notice getNotice(int type) {
        for (Notice notice : Notice.values()) {
            if (notice.getType() == type) {
                return notice;
            }
        }
        return UNKNOWN;
    }
}
