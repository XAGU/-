package com.xiaolian.amigo.data.network.model.repair;

import lombok.Data;

/**
 * 网络请求-设备报修
 *
 * @author caidong
 * @date 17/9/18
 */
@Data
public class RepairReqDTO {

    /**
     * 页码
     */
    private Integer page;
    /**
     * 页大小
     */
    private Integer size;

}
