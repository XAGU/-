package com.xiaolian.amigo.data.network.model.order;

import java.io.Serializable;

import lombok.Data;

/**
 * 订单
 *
 * @author caidong
 * @date 17/9/15
 */
@Data
public class Order implements Serializable {
    private Long id;
    /**
     * 消费金额
     */
    private String consume;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * 设备类型，1 - 热水澡， 2 - 饮水机，3 - 洗衣机，4 - 吹风机，
     */
    private Integer deviceType;
    /**
     * 设备位置
     */
    private String location;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 预付金额
     */
    private String prepay;
    /**
     * 学校id
     */
    private Integer schoolId;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 订单状态， 1 - 使用中， 2 - 已结束 3 - 异常
     */
    private Integer status;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用水量
     */
    private Integer waterUsage;
    /**
     * 找零
     */
    private String odd;
    /**
     * 设备mac地址
     */
    private String macAddress;
    /**
     * 代金券
     */
    private String bonus;
    /**
     * 实际扣款
     */
    private String actualDebit;
    /**
     * 是否是最低消费
     */
    private Boolean lowest;
    private Long residenceId;
    /**
     * 供应商
     */
    private Long supplierId;

    /**
     * 设备工作模式
     */
    private Integer mode;
    /**
     * 工作模式描述
     */
    private String modeDesc;
    /**
     * 水温
     */
    private String usefor;

    private Integer category;
}
