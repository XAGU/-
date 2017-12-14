package com.xiaolian.amigo.data.network.model.device;

import java.util.List;

import lombok.Data;

/**
 * 饮水机
 * <p>
 * Created by zcd on 17/12/14.
 */
@Data
public class WaterInListDTO {
    /**
     * 是否已收藏
     */
    private Boolean favor;

    private String location;

    private Long residenceId;

    private List<DeviceInListDTO> water;
}
