package com.xiaolian.amigo.data.network.model.device;

import java.util.List;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 18/1/3.
 */
@Data
public class QueryDeviceListReqDTO {
    private List<String> macAddresses;
    private Integer type;
    private Integer page;
    private Integer size;
}
