package com.xiaolian.amigo.data.network.model.common;

import lombok.Data;

/**
 * @author zcd
 * @date 17/9/18
 */
@Data
public class BooleanRespDTO {
    private boolean result;   //  结果
    private String failReason ;   // 失败原因

}
