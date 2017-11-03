package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.IWalletApi;

/**
 * 我的钱包
 * <p>
 * Created by zcd on 9/18/17.
 */

public interface IWalletDataManager extends IWalletApi {
    void setLastWithdrawId(Long id);

    Long getLastWithdrawId();

    void setLastWithdrawName(String name);

    String getLastWithdrawName();

    String getLastRechargeAmount();

    void setLastRechargeAmount(String amount);
}
