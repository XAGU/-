package com.xiaolian.amigo.data.network.model.userbill;

import lombok.Data;

@Data
public class QueryBillListReqDTO {
    private String date;
    private Long lastId;
    private boolean pageDown;
    private Integer status;
    private Integer type;
    private int size;
}
