package com.xiaolian.amigo.data.network.model.timerange;

import lombok.Data;

/**
 * 查询供水时间段
 *
 * @author zcd
 * @date 17/9/25
 */
@Data
public class QueryTimeValidRespDTO {
    private String remark;
    private String title;
    private boolean valid;
}
