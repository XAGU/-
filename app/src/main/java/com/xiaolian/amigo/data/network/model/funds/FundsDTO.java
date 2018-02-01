package com.xiaolian.amigo.data.network.model.funds;

import lombok.Data;

/**
 * 充值提现详情
 *
 * @author zcd
 * @date 17/10/27
 */
@Data
public class FundsDTO {
    private String amount;
    private Long createTime;
    private Long id;
    private Integer operationType;
    private String orderNo;
    private String reason;
    private Integer status;
    private Integer thirdAccountType;
    private String thirdAccountName;
    private String csMobile;
    private Boolean instead;
}
