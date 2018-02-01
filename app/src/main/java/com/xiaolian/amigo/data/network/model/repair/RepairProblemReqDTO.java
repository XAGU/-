package com.xiaolian.amigo.data.network.model.repair;

import lombok.Data;

/**
 * 网络请求- 报修问题
 *
 * @author caidong
 * @date 17/9/20
 */
@Data
public class RepairProblemReqDTO {

    /**
     * 页码
     */
    Integer page;
    /**
     * 页大小
     */
    Integer size;
    Integer deviceType;
}
