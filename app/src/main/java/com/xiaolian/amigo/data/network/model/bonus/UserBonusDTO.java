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
}
