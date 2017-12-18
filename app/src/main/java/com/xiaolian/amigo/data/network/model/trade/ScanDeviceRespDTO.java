package com.xiaolian.amigo.data.network.model.trade;

import com.xiaolian.amigo.data.vo.ScanDeviceGroup;

import java.util.List;

import lombok.Data;

/**
 * Created by caidong on 2017/10/16.
 */
@Data
public class ScanDeviceRespDTO {

    private Integer total;

    private List<ScanDeviceGroup> devices;

}
