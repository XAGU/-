package com.xiaolian.amigo.data.network.model.bonus;

import com.xiaolian.amigo.data.vo.Bonus;
import com.xiaolian.amigo.data.vo.Mapper;

import lombok.Data;

/**
 * 代金券
 *
 * @author zcd
 * @date 17/12/14
 */
@Data
public class UserBonusDTO implements Mapper<Bonus> {
    private Double amount;
    private String createTime;
    private Integer deviceType;
    private Long endTime;
    private Long id;
    private String name;
    private String remarks;
    private String description;
    private Long timeLimit;
    private String updateTime;
    private Integer useStatus;
    private Integer validStatus;
    /**
     * 生效时间
     */
    private Long startTime;

    @Override
    public Bonus transform() {
        Bonus bonus = new Bonus();
        bonus.setAmount(amount);
        bonus.setCreateTime(createTime);
        bonus.setDeviceType(deviceType);
        bonus.setEndTime(endTime);
        bonus.setId(id);
        bonus.setName(name);
        bonus.setRemarks(remarks);
        bonus.setDescription(description);
        bonus.setTimeLimit(timeLimit);
        bonus.setUpdateTime(updateTime);
        bonus.setUseStatus(useStatus);
        bonus.setValidStatus(validStatus);
        bonus.setStartTime(startTime);
        return bonus;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
}
