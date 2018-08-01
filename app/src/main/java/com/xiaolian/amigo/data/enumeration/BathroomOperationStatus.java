package com.xiaolian.amigo.data.enumeration;

/**
 * 公共浴室相关操作状态
 *
 * @author zcd
 * @date 18/7/13
 */
public enum BathroomOperationStatus {
    /**
     * 初始化
     */
    INIT(1),
    /**
     * 已经受理
     */
    ACCEPTED(2),
    /**
     * 预约失败
     */
    FAIL(3),
    /**
     * 取消
     */
    CANCELED(4),
    /**
     * 过期
     */
    EXPIRED(5),
    /**
     * 已经开阀，同时支付
     */
    OPENED(6),
    /**
     * 已经结算
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
                return INIT;
            case 2 :
                return ACCEPTED;
            case 3:
                return FAIL;
            case 4:
                return CANCELED;
            case 5:
                return EXPIRED;
            case 6:
                return OPENED;
            case 7:
                return FINISHED;
                default:
                    return null ;
        }
    }
}
