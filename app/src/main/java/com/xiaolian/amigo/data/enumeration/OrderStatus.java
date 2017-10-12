package com.xiaolian.amigo.data.enumeration;

/**
 * 订单状态
 *
 * Created by caidong on 2017/10/12.
 */
public enum OrderStatus {
    USING(1), FINISHED(2);

    private Integer code;

    OrderStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static OrderStatus getOrderStatus(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}
