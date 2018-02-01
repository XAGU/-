package com.xiaolian.amigo.data.network.model.funds;

import com.xiaolian.amigo.data.network.model.wallet.RechargeDenominations;

import java.util.List;

import lombok.Data;

/**
 * 充值金额
 *
 * @author zcd
 * @date 17/9/20
 */
@Data
public class QueryRechargeAmountsRespDTO {
    List<RechargeDenominations> rechargeDenominations;
    Integer total;
}
