package com.xiaolian.amigo.data.network.model.dto.request;

import java.util.List;

import lombok.Data;

/**
 * Created by caidong on 2017/10/16.
 */
@Data
public class ScanDeviceReqDTO {

    private List<String> macAddresses;

}
