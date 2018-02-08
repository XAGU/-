package com.xiaolian.amigo.data.vo;

import java.util.List;

import lombok.Data;

/**
 * @author caidong
 * @date 17/10/16
 */
@Data
public class ScanDeviceGroup {
    /**
     * 是否已收藏
     */
    private Boolean favor;

    private String location;

    private Integer type;

    private Long residenceId;

    private List<ScanDevice> water;
}
