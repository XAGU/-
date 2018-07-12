package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * 公共浴室预约
 *
 * @author zcd
 * @date 18/7/12
 */
@Data
public class BathOrderRespDTO {
    /**
     * 预约订单id，购买编码不会生成
     */
    private Long bathOrderId;
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
    private Double amount;
    /**
     * 用户余额
     */
    private Double balance;
    /**
     * 编码:购买编码方式会生成
     */
    private String code;
    /**
     * 一个随机红包
     */
    private BriefBonusDTO bonus;
    /**
     * 1 预约中（未支付） 2 预约成功  3 预约失败
     */
    private Integer status;
    /**
     * 失约次数 只有预约才会返回
     */
    private Integer missTimes;
}
