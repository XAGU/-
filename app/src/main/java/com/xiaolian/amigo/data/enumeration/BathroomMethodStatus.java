package com.xiaolian.amigo.data.enumeration;

/**
 * 学校能够使用的浴室方式
 */
public enum  BathroomMethodStatus {

    /**
     * 预约
     */
    BOOKING(1),

    /**
     * 购买编码
     */
    BUY_CODE(2),

    /**
     * 扫一扫
     */
    SCANNING(3),

    /**
     * 直接使用
     */
    USING(4);

    BathroomMethodStatus(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public static BathroomMethodStatus  getBathroomMethodStatus(int code){
        switch (code){
            case 1:
                return BathroomMethodStatus.BOOKING;

            case 2:
                return BathroomMethodStatus.BUY_CODE;

            case 3:
                return SCANNING;

            case 4:
                return USING;
                default:
                    return null;

        }

    }

}
