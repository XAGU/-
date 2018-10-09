package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * 学校业务
 *
 * @author zcd
 * @date 17/10/11
 */
@Data
public class BriefSchoolBusiness {
    private Long businessId;
    private Boolean using;
    private Integer prepayOrder;
    private Boolean publicBath ;
}
