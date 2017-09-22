package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.IWalletApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalWalletDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryRechargeAmountsRespDTO;

import javax.inject.Inject;

import rx.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
 * 我的钱包
 * <p>
 * Created by zcd on 9/18/17.
 */

public class WalletDataManager implements IWalletDataManager {

    private static final String TAG = WalletDataManager.class.getSimpleName();

    private IWalletApi walletApi;

    @Inject
    public WalletDataManager(Retrofit retrofit) {
        walletApi = retrofit.create(IWalletApi.class);
    }

    @Override
    public Observable<ApiResult<PersonalWalletDTO>> queryWallet() {
        return walletApi.queryWallet();
    }

    @Override
    public Observable<ApiResult<QueryRechargeAmountsRespDTO>> queryRechargeAmountList(@Body SimpleQueryReqDTO body) {
        return walletApi.queryRechargeAmountList(body);
    }
}
