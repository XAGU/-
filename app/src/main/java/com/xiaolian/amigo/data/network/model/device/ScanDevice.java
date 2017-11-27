package com.xiaolian.amigo.data.network.model.device;

import lombok.Data;

/**
 * Created by caidong on 2017/10/16.
 */
@Data
public class ScanDevice {

    private Long id;

    private String hardwareNo;

    private String macAddress;

    private Integer type;

    private Double price;

    private Integer pulse;

    private String usefor;
}
