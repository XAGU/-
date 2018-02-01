package com.xiaolian.amigo.data.network.model.device;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/1/3
 */
@Data
public class QueryFavorDeviceRespDTO {
    private List<FavorDeviceDTO> devices;
    private Integer total;
}
