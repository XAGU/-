package com.xiaolian.amigo.data.network.model.device;

import java.util.Map;

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

    private Map<String, ScanDevice> water;
}
