package com.xiaolian.amigo.data.network.model.repair;

import java.util.List;

import lombok.Data;

/**
 * 网络请求 - 设备报修
 * <p>
 * Created by caidong on 2017/9/20.
 */
@Data
public class RepairApplyReqDTO {

    // 报修描述
    private String description;
    // 设备类型
    private Integer deviceType;
    // 手机号码
    private Long mobile;
    // 宿舍或位置id
    private Long residenceId;
    // 设备问题列表
    private List<Long> causeIds;
    // 设备报修图片列表
    private List<String> images;


}
