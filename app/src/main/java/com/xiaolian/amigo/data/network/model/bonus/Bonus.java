package com.xiaolian.amigo.data.network.model.bonus;

import lombok.Data;

/**
 * 红包
 * @author zcd
 */
@Data
public class Bonus {
    private Long amount;
    private String createTime;
    private Integer deviceType;
    private String endTime;
    private Long id;
    private String name;
    private String remarks;
    private Long timeLimit;
    private String updateTime;
    private Integer useStatus;
    private Integer validStatus;
}
