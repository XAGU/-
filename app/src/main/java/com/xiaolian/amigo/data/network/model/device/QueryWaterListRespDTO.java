package com.xiaolian.amigo.data.network.model.device;

import java.util.List;

import lombok.Data;

/**
 * 个人收藏饮水机列表
 *
 * @author zcd
 * @date 17/12/14
 */
@Data
public class QueryWaterListRespDTO {
    private List<WaterInListDTO> devices;
}
