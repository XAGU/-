package com.xiaolian.amigo.data.network.model.bonus;

import lombok.Data;

/**
 * 红包
 * @author zcd
 */
@Data
public class Bonus {
    private int amount;
    private String createTime;
    private int deviceType;
    private String endTime;
    private int id;
    private String name;
    private String remarks;
    private int timeLimit;
    private String updateTime;
    private int useStatus;
    private int validStatus;
}
