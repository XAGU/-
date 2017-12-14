package com.xiaolian.amigo.data.network.model.device;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 17/12/14.
 */
@Data
public class DeviceInListDTO {
    private Long id;

    private String hardwareNo;

    private String macAddress;

    private Integer type;

    private Double price;

    private Integer pulse;

    private String usefor;
}
