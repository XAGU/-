package com.xiaolian.amigo.data.network.model.school;

import lombok.Data;

/**
 * @author zcd
 * @date 18/3/21
 */
@Data
public class QuerySchoolListReqDTO {
    private Integer page;
    private Integer size;
    private Boolean online;
}
