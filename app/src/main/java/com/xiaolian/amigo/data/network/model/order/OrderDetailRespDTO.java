package com.xiaolian.amigo.data.network.model.order;

import lombok.Data;

/**
 * 网络返回-订单详情
 *
 * @author caidong
 * @date 17/10/11
 */
@Data
public class OrderDetailRespDTO {
    private Long id;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 设备地址
     */
    private String location;
    /**
     * 找零
     */
    private String odd;
    /**
     * 实际消费
     */
    private String consume;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 预付金额
     */
    private String prepay;
    /**
     * 设备类型
     */
    private Integer deviceType;
    /**
     * status为3表示异常账单
     */
    private Integer status;
    /**
     * 代金券
     */
    private String bonus;
    /**
     * 实际扣款
     */
    private String actualDebit;
    /**
     * 是否是最低消费
     */
    private Boolean lowest;
    /**
     * 使用时长
     */
    private String useTime;

    /**
     * 设备工作模式
     */
    private Integer mode;
    /**
     * 工作模式描述
     */
    private String modeDesc;
    /**
     * 二维码信息
     */
    private String qrCode;

    private Boolean favor;
    private String macAddress;
    private Long residenceId;
    private Integer usefor;
    /**
     * 零元消费额文案
     */
    private String zeroConsumeCopy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOdd() {
        return odd;
    }

    public void setOdd(String odd) {
        this.odd = odd;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getPrepay() {
        return prepay;
    }

    public void setPrepay(String prepay) {
        this.prepay = prepay;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getActualDebit() {
        return actualDebit;
    }

    public void setActualDebit(String actualDebit) {
        this.actualDebit = actualDebit;
    }

    public Boolean getLowest() {
        return lowest;
    }

    public void setLowest(Boolean lowest) {
        this.lowest = lowest;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getModeDesc() {
        return modeDesc;
    }

    public void setModeDesc(String modeDesc) {
        this.modeDesc = modeDesc;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Boolean getFavor() {
        return favor;
    }

    public void setFavor(Boolean favor) {
        this.favor = favor;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Long getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Long residenceId) {
        this.residenceId = residenceId;
    }

    public Integer getUsefor() {
        return usefor;
    }

    public void setUsefor(Integer usefor) {
        this.usefor = usefor;
    }

    public String getZeroConsumeCopy() {
        return zeroConsumeCopy;
    }

    public void setZeroConsumeCopy(String zeroConsumeCopy) {
        this.zeroConsumeCopy = zeroConsumeCopy;
    }
}
