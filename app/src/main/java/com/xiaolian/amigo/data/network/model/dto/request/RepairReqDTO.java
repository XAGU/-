package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 网络请求-设备报修
 * <p>
 * Created by caidong on 2017/9/18.
 */
@Data
public class RepairReqDTO {

    // 页码
    private int page;
    // 页大小
    private int size;

}
