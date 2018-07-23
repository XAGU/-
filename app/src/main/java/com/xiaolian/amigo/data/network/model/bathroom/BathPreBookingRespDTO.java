package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * 公共浴室预约
 *
 * @author zcd
 * @date 18/7/17
 */
@Data
public class BathPreBookingRespDTO {
    /**
     * 设备位置
     */
    private String location;
    /**
     * 过期时间
     */
    private Long expiredTime;
    /**
     * 预付金额
     */
    private Double prepay;
    /**
     * 最小预付金额
     */
    private Double minPrepay;
    /**
     * 用户余额
     */
    private Double balance;
    /**
     * 一个随机红包
     */
    private BriefBonusDTO bonus;
    /**
     * 失约次数 只有预约才会返回
     */
    private Integer missedTimes;
    /**
     * 总共可失约次数 只有预约才会返回
     */
    private Integer maxMissAbleTimes;
    /**
     * 预留时间
     */
    private String reservedTime;
}
