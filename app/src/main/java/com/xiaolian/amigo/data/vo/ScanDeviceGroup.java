package com.xiaolian.amigo.data.vo;

import java.util.List;

import lombok.Data;

/**
 * @author caidong
 * @date 17/10/16
 */
public class ScanDeviceGroup {
    /**
     * 1、普通饮水机 2、三合一饮水机
     **/
    private Integer category;
    /**
     * 是否已收藏
     */
    private Boolean favor;

    private String location;

    private Integer type;

    private Long residenceId;

    private List<ScanDevice> water;

    /**
     * 下单前文案
     */
    private List<String> afterOrderCopy ;

    /**
     * 下单后文案
     */
    private List<String> preOrderCopy ;

    public List<String> getAfterOrderCopy() {
        return afterOrderCopy;
    }

    public void setAfterOrderCopy(List<String> afterOrderCopy) {
        this.afterOrderCopy = afterOrderCopy;
    }

    public List<String> getPreOrderCopy() {
        return preOrderCopy;
    }

    public void setPreOrderCopy(List<String> preOrderCopy) {
        this.preOrderCopy = preOrderCopy;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Boolean getFavor() {
        return favor;
    }

    public void setFavor(Boolean favor) {
        this.favor = favor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Long residenceId) {
        this.residenceId = residenceId;
    }

    public List<ScanDevice> getWater() {
        return water;
    }

    public void setWater(List<ScanDevice> water) {
        this.water = water;
    }
}
