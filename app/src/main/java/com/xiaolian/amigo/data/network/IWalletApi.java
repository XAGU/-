package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalWalletDTO;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * 我的钱包
 * <p>
 * Created by zcd on 9/18/17.
 */

public interface IWalletApi {

    // 获取余额
    @POST("/funds/wallet/personal/one")
    Observable<ApiResult<PersonalWalletDTO>> queryWallet();
}
