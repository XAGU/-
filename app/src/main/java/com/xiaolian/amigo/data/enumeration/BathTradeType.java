package com.xiaolian.amigo.data.enumeration;

/**
 * @author zcd
 * @date 18/7/10
 */
public enum BathTradeType {
    /**
     * 预约使用(预约指定房间)
     */
    BOOKING_DEVICE(1),
    /**
     * 预约不锁定设备
     */
    BOOKING_WITHOUT_DEVICE(2),
    /**
     * 扫一扫
     */
    QR_CODE(3),
    /**
     * 直接使用
     */
    DIRECT(4);

    private int code;

    BathTradeType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static BathTradeType getBathTradeType(int type) {
        for (BathTradeType tradeType : BathTradeType.values()) {
            if (tradeType.getCode() == type) {
                return tradeType;
            }
        }
        return null;
    }
}
