package com.xiaolian.amigo.data.network.model.userbill;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/6
 */
@Data
public class QueryPersonalMaxConsumeOrderListReqDTO {
    private Integer month;
    private Integer year;
}
