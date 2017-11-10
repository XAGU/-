package com.xiaolian.amigo.data.network.model.dto.request;

import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by caidong on 2017/10/16.
 */
@Data
public class ScanDeviceReqDTO {

    private List<String> macAddresses;

}
