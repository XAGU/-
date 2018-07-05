package com.xiaolian.amigo.data.network.model.bathroom;

/**
 * 公共浴室预约
 *
 * @author zcd
 * @date 18/7/4
 */
public class BathBookingRespDTO {
    private Long bookingId;
    /**
     * 设备位置
     */
    private String location;
    /**
     * 过期时间
     */
    private Long  expiredTime;
    /**
     * 预付金额
     */
    private Double amount;
    /**
     * 红包
     */
    private BriefBonusDTO bonus;

    /**
     * 1 预约中（未支付） 2 预约成功  3 预约失败
     */
    private Integer status;
}
