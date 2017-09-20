package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.wallet.RechargeDenominations;

import java.util.List;

import lombok.Data;

/**
 * 充值金额
 * <p>
 * Created by zcd on 9/20/17.
 */
@Data
public class QueryRechargeAmountsRespDTO {
    List<RechargeDenominations> rechargeDenominations;
    Integer total;
}
