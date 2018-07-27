package com.xiaolian.amigo.data.enumeration;

/**
 * 公共浴室相关操作状态
 *
 *INIT(1),
 ACCEPTED(2),
 BOOKING_FAIL(3),
 CANCELED(4),
 EXPIRED(5),
 OPENED(6),
 FINISHED(7);  还未更新的状态
 *
 *    1 、  订单初始化 （预约订单 ， 支付之后）   2、下发成功（预约）  购买成功（洗澡卷）3  、 失败    4、 取消
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



    public static BathroomOperationStatus getBathroomOperationStatus(int code){
        switch (code){
            case 1:
                return CANCEL;
            case 2 :
                return PAYED;
            case 3:
                return BOOKING_SUCCESS;
            case 4:
                return BOOKING_FAIL;
            case 5:
                return EXPIRED;
            case 6:
                return USING;
            case 7:
                return FINISHED;
                default:
                    return null ;
        }
    }
}
