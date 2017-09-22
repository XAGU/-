package com.xiaolian.amigo.data.network.model.repair;

import lombok.Data;

/**
 * 报修信息
 * <p>
 * Created by caidong on 2017/9/12.
 */
@Data
public class Repair {

    // 报修时间
    private Long createTime;
    // 报修设备类型
    private Integer deviceType;
    // 报修id
    private Long id;
    // 设备位置
    private String location;
    // 设备状态，1 - 正常， 2 - 报修中
    private Integer status;
    // 设备编号
    private String hardwareNo;
    // 评价状态，1 - 未评价， 2 - 已评价
//    private Integer evaluateStatus;
    private Integer rated;
    // 维修人员名称
    private String repairmanName;
    // 维修人员Id
    private Long repairmanId;

}
