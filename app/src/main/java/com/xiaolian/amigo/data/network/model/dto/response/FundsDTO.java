package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.Data;

/**
 * 充值提现详情
 * <p>
 * Created by zcd on 10/27/17.
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
}
