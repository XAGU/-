package com.xiaolian.amigo.data.network.model.funds;

import lombok.Data;

/**
 * 我的钱包DTO
 *
 * @author zcd
 * @date 17/9/18
 */
@Data
public class PersonalWalletDTO {
    private Double balance;
    private Double prepay;
}
