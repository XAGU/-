package com.xiaolian.amigo.data.vo;

import com.xiaolian.amigo.data.network.model.device.Supplier;

import java.util.List;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 17/12/18.
 */
@Data
public class DeviceCategory {
    private Integer deviceType;
    private List<Supplier> suppliers;
}
