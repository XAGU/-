package com.xiaolian.amigo.data.vo;

import lombok.Data;

/**
 * Created by caidong on 2017/10/16.
 */
@Data
public class ScanDevice {
    private Long id;
    private String macAddress;
    private Integer price;
    private Integer pulse;
    private String usefor;
    private String name;
}
