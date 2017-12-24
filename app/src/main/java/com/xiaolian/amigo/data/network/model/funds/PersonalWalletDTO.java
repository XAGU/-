package com.xiaolian.amigo.data.network.model.funds;

import lombok.Data;

/**
 * 我的钱包DTO
 * <p>
 * Created by zcd on 9/18/17.
 */
@Data
public class PersonalWalletDTO {
    private Double balance;
    private Double prepay;
}
