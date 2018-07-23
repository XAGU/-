package com.xiaolian.amigo.data.enumeration;

/**
 * 公共浴室相关操作状态
 *
 * @author zcd
 * @date 18/7/13
 */
public enum BathroomOperationStatus {
    /**
     * 取消  ， 取消预约
     */
    CANCEL(1),
    /**
     * 已支付 ，
     */
    PAYED(2),
    /**
     * 预约成功（下发成功）  等待洗浴
     */
    BOOKING_SUCCESS(3),
    /**
     * 预约失败 (已经失败，并已经退款)
     */
    BOOKING_FAIL(4),
    /**
     * 已经过期(已经退款)    预约超时
     */
    EXPIRED(5),
    /**
     * 正在使用
     */
    USING(6),
    /**
     * 已经完成    洗浴结束
     */
    FINISHED(7);


    BathroomOperationStatus(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }
}
