package com.xiaolian.amigo.data.network.model.funds;

import lombok.Data;

/**
 * 提现reqDTO
 *
 * @author zcd
 * @date 17/10/14
 */
@Data
public class WithdrawReqDTO {
    private String amount;
    private Long userThirdAccountId;
}
