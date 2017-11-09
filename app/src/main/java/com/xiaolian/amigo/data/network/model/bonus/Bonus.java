package com.xiaolian.amigo.data.network.model.bonus;

import lombok.Data;

/**
 * 红包
 * @author zcd
 */
@Data
public class Bonus {
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
    // 生效时间
    private Long startTime;
}
