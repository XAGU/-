package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

@Data
public class QueryBathOrderListReqDTO {

    private int page ;
    private int size ;
    private int type ;
    private Long userId ;
}
