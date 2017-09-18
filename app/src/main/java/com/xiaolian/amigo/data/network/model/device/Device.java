package com.xiaolian.amigo.data.network.model.device;

import lombok.Data;

/**
 * 设备信息
 * <p>
 * Created by caidong on 2017/9/18.
 */
@Data
public class Device {
    // 绑定时间
    private String bindingTime;
    // 硬件编号
    private String hardwareNo;
    // 健康状态 1 - 正常， 2 - 报修中
    private int healthStatus;
    // 设备id
    private int id;
    // 纬度
    private double lat;
    // 位置
    private String location;
    // 经度
    private double lon;
    // 设备所在宿舍id
    private int residenceId;
    // 设备所在学校id
    private int schoolId;
    // 设备所在学校名称
    private String schoolName;
    // 设备类型，1 - 热水器， 2 - 饮水机
    private int type;
    // 设备用途
    private int usefor;
}
