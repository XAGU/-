package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.Data;

/**
 * 我的钱包DTO
 * <p>
 * Created by zcd on 9/18/17.
 */
@Data
public class PersonalWalletDTO {
    private Long balance;
    private Long prepay;
}
