package com.xiaolian.amigo.data.vo;

import java.util.List;

import lombok.Data;

/**
 * Created by caidong on 2017/10/16.
 */
@Data
public class ScanDeviceGroup {
    /**
     * 是否已收藏
     */
    private Boolean favor;

    private String location;

    private Long residenceId;

//    private Map<String, ScanDevice> water;
    private List<ScanDevice> water;
}
