package com.xiaolian.amigo.data.network.model.trade;

import lombok.Data;

/**
 * 支付请求
 *
 * @author caidong
 * @date 17/10/9
 */
@Data
public class PayReqDTO {

    /**
     * 设备mac地址
     */
    private String macAddress;

    /**
     * 预付金额
     */
    private Double prepay;

    /**
     * 代金券id
     */
    private Long bonusId;

    private Integer mode;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Double getPrepay() {
        return prepay;
    }

    public void setPrepay(Double prepay) {
        this.prepay = prepay;
    }

    public Long getBonusId() {
        return bonusId;
    }

    public void setBonusId(Long bonusId) {
        this.bonusId = bonusId;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}
