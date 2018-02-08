package com.xiaolian.amigo.data.vo;

import lombok.Data;

/**
 * @author caidong
 * @date 17/10/16
 */
@Data
public class ScanDevice {
    private Long id;
    private String macAddress;
    private Integer price;
    private Integer pulse;
    private String usefor;
    private String name;
    private Long supplierId;
}
