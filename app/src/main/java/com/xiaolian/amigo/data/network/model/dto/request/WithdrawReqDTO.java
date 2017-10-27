package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 提现reqDTO
 * <p>
 * Created by zcd on 10/14/17.
 */
@Data
public class WithdrawReqDTO {
    private String amount;
    private Long userThirdAccountId;
}
