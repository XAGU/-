package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.device.Device;
import com.xiaolian.amigo.data.network.model.order.Order;

import java.util.List;

import lombok.Data;

/**
 * 网络返回 - 收藏设备
 * <p>
 * Created by caidong on 2017/9/15.
 */
@Data
public class FavoriteRespDTO {

    // 收藏设备总数
    private Integer total;
    // 收藏设备列表
    private List<Device> devices;

}
