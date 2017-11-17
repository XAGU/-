package com.xiaolian.amigo.data.network.model.bonus;

import java.io.Serializable;

import lombok.Data;

/**
 * 代金券
 * @author zcd
 */
@Data
public class Bonus implements Serializable {
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
