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
    private Double allBalance;
    private Double prepay;
    private Double chargeBalance;
    private Double givingBalance;
    private String givingRule;
    private String useLimit;
    private Boolean existGiving;

    public Double getAllBalance() {
        if (allBalance == null) {
            return 0.0;
        }
        return allBalance;
    }

    public Boolean getExistGiving() {
        if (existGiving == null) {
            return false;
        }
        return existGiving;
    }

    public Double getChargeBalance() {
        if (chargeBalance == null) {
            return 0.0;
        }
        return chargeBalance;
    }

    public Double getGivingBalance() {
        if (givingBalance == null) {
            return 0.0;
        }
        return givingBalance;
    }
    /**
     * 是否显示提现入口
     */
    private boolean showWithdraw;
}
