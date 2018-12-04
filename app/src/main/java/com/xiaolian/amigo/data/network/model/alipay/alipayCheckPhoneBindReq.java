package com.xiaolian.amigo.data.network.model.alipay;

import lombok.Data;

@Data
public class alipayCheckPhoneBindReq {

    /**
     * alipayUserId : 0
     * appVersion : string
     * brand : string
     * code : string
     * edition : 0
     * mobile : 0
     * model : string
     * system : 0
     * systemVersion : string
     * uniqueId : string
     */

    private int alipayUserId;
    private String appVersion;
    private String brand;
    private String code;
    private int edition;
    private int mobile;
    private String model;
    private int system;
    private String systemVersion;
    private String uniqueId;
}
