package com.xiaolian.amigo.data.network.model.repair;

/**
 * 报修信息
 */
public class Repair {

    // 报修时间
    private String createTime;
    // 报修设备类型
    private Integer deviceType;
    // 报修id
    private int id;
    // 设备位置
    private String location;
    // 设备状态，1 - 正常， 2 - 报修中
    private int status;
    // 设备编号
    private String hardwareNo;

}
