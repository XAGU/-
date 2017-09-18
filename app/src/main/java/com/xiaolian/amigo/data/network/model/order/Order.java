package com.xiaolian.amigo.data.network.model.order;

import java.io.Serializable;

import lombok.Data;

/**
 * 订单
 * <p>
 * Created by caidong on 2017/9/15.
 */
@Data
public class Order implements Serializable {
    private int id;
    // 消费金额
    private int consume;
    // 创建时间
    private String createTime;
    // 设备id
    private int deviceId;
    // 设备编号
    private String deviceNo;
    // 设备类型，1 - 热水器， 2 - 饮水机，3 - 洗衣机，4 - 吹风机，
    private int deviceType;
    // 设备位置
    private String location;
    // 订单编号
    private String orderNo;
    // 支付类型，1 - 余额支付， 2 - 红包支付
    private int paymentType;
    // 预付金额
    private int prepay;
    // 学校id
    private int schoolId;
    // 学校名称
    private String schoolName;
    // 订单状态， 1 - 使用中， 2 - 已结束
    private int status;
    // 用户id
    private int userId;
    // 用户名
    private String username;
    // 用水量
    private int waterUsage;
}
