package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.bonus.Bonus;
import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by caidong on 2017/10/16.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanDeviceRespDTO {

    private Integer total;

    private List<ScanDeviceGroup> devices;

}
