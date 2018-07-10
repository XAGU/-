package com.xiaolian.amigo.data.enumeration;

/**
 * @author zcd
 * @date 18/7/10
 */
public enum BathTradeType {
    /**
     * 预约使用
     */
    BOOKING(1),
    /**
     * 购买编码
     */
    BUY_CODE(2),
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
