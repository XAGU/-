package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 网络请求- 报修问题
 *
 * Created by caidong on 2017/9/20.
 */
@Data
public class RepairProblemReqDTO {

    // 页码
    Integer page;
    // 页大小
    Integer size;
    Integer deviceType;
}
