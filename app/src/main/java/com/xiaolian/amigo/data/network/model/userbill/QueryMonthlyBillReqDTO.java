package com.xiaolian.amigo.data.network.model.userbill;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/5
 */
@Data
public class QueryMonthlyBillReqDTO {
    private Integer month;
    private Integer year;
}
