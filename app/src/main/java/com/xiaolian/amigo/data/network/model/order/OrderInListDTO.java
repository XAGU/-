package com.xiaolian.amigo.data.network.model.order;

import com.xiaolian.amigo.data.vo.Mapper;

import lombok.Data;

/**
 * 订单
 *
 * @author zcd
 * @date 17/12/14
 */
public class OrderInListDTO implements Mapper<Order> {
    private Long id;
    /**
     * 消费金额
     */
    private String consume;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * 设备类型，1 - 热水澡， 2 - 饮水机，3 - 洗衣机，4 - 吹风机，
     */
    private Integer deviceType;
    /**
     * 设备位置
     */
    private String location;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 预付金额
     */
    private String prepay;
    /**
     * 学校id
     */
    private Integer schoolId;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 订单状态， 1 - 使用中， 2 - 已结束 3 - 异常
     */
    private Integer status;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用水量
     */
    private Integer waterUsage;
    /**
     * 找零
     */
    private String odd;
    /**
     * 设备mac地址
     */
    private String macAddress;
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
    private Long residenceId;
    /**
     * 供应商id
     */
    private Long supplierId;
    private String usefor;
    private Integer category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPrepay() {
        return prepay;
    }

    public void setPrepay(String prepay) {
        this.prepay = prepay;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getWaterUsage() {
        return waterUsage;
    }

    public void setWaterUsage(Integer waterUsage) {
        this.waterUsage = waterUsage;
    }

    public String getOdd() {
        return odd;
    }

    public void setOdd(String odd) {
        this.odd = odd;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
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

    public Long getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Long residenceId) {
        this.residenceId = residenceId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getUsefor() {
        return usefor;
    }

    public void setUsefor(String usefor) {
        this.usefor = usefor;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getExceptionOrderCopy() {
        return exceptionOrderCopy;
    }

    public void setExceptionOrderCopy(String exceptionOrderCopy) {
        this.exceptionOrderCopy = exceptionOrderCopy;
    }

    public String getZeroConsumeCopy() {
        return zeroConsumeCopy;
    }

    public void setZeroConsumeCopy(String zeroConsumeCopy) {
        this.zeroConsumeCopy = zeroConsumeCopy;
    }

    /**
     * 异常订单文案
     */
    private String exceptionOrderCopy;
    /**
     * 零元消费额文案
     */
    private String zeroConsumeCopy;

    /**
     * 账单状态
     */
    private Integer billStatus ;


    @Override
    public Order transform() {
        Order order = new Order();
        order.setActualDebit(actualDebit);
        order.setBonus(bonus);
        order.setMacAddress(macAddress);
        order.setOdd(odd);
        order.setWaterUsage(waterUsage);
        order.setUserId(userId);
        order.setUsername(username);
        order.setStatus(status);
        order.setSchoolId(schoolId);
        order.setSchoolName(schoolName);
        order.setPrepay(prepay);
        order.setOrderNo(orderNo);
        order.setLocation(location);
        order.setDeviceType(deviceType);
        order.setDeviceNo(deviceNo);
        order.setDeviceId(deviceId);
        order.setCreateTime(createTime);
        order.setConsume(consume);
        order.setId(id);
        order.setLowest(lowest);
        order.setResidenceId(residenceId);
        order.setSupplierId(supplierId);
        order.setUsefor(usefor);
        order.setCategory(category);
        order.setZeroConsumeCopy(zeroConsumeCopy);
        order.setExceptionOrderCopy(exceptionOrderCopy);
        order.setBillStatus(billStatus);
        return order;
    }
}
