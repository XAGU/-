package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalWalletDTO;

import rx.Observable;

/**
 * 设备数据管理
 * <p>
 * Created by zcd on 9/29/17.
 */

public interface IDeviceDataManager {
    void setBonusAmount(int amount);

    int getBonusAmount();

    // 获取余额
    Observable<ApiResult<PersonalWalletDTO>> queryWallet();
}
