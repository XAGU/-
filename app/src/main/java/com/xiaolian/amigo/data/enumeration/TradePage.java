package com.xiaolian.amigo.data.enumeration;

/**
 * @author  wcm
 * 交易页面枚举
 */
public enum  TradePage {

    /**
     * 蓝牙
     */
    BLE("BLE") ,
    /**
     * 网关通讯交易（比如公共浴室）
     */
    GATEWAY_NETWORK("GATEWAY_NETWORK") ,
    /**
     * 二维码交易
     */
    QR_CODE("QR_CODE"),
    /**
     * NB
     */
    NB("NB");


    private String page ;

    TradePage(String page){
        this.page = page ;
    }

    public String getPage() {
        return page;
    }

    public static TradePage getTradePage(String page) {
        for (TradePage tradePage : TradePage.values()) {
            if (tradePage.getPage().equals(page) ) {
                return tradePage;
            }
        }
        return BLE;
    }
}
