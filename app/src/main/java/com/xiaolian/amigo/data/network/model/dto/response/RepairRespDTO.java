package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.data.network.model.repair.Repair;

import java.util.List;

/**
 * 网络返回 - 设备报修
 * <p>
 * Created by caidong on 2017/9/15.
 */
public class RepairRespDTO {

    // 报修总数
    private int total;
    // 报修列表
    private List<Repair> repairDevices;

}
