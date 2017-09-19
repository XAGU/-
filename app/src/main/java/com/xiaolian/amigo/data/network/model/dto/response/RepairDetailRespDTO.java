package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.repair.RepairStep;

import java.util.List;

import lombok.Data;

/**
 * 网络返回 - 设备详情
 * <p>
 * Created by caidong on 2017/9/15.
 */
@Data
public class RepairDetailRespDTO {

    // 报修内容
    private String content;
    // 设备类型
    private int deviceType;
    // 设备位置
    private String location;
    // 报修图片组url
    private List<String> images;
    // 报修进度列表
    private List<RepairStep> steps;

}
