package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.IWalletApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.AddThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.AlipayTradeAppPayArgsReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.AlipayTradeAppPayResultParseReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryPersonalFundsListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryTimeValidReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryUserThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RechargeReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.WithdrawReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.AlipayTradeAppPayArgsRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.AlipayTradeAppPayResultParseRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.FundsDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalWalletDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryFundsListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryRechargeAmountsRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryTimeValidRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryUserThirdAccountRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;

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

    @Override
    public Observable<ApiResult<QueryTimeValidRespDTO>> queryWithDrawTimeValid() {
        return walletApi.queryWithDrawTimeValid();
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> recharge(@Body RechargeReqDTO reqDTO) {
        return walletApi.recharge(reqDTO);
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> withdraw(@Body WithdrawReqDTO reqDTO) {
        return walletApi.withdraw(reqDTO);
    }

    @Override
    public Observable<ApiResult<QueryFundsListRespDTO>> queryWithdrawList(@Body QueryPersonalFundsListReqDTO reqDTO) {
        return walletApi.queryWithdrawList(reqDTO);
    }

    @Override
    public Observable<ApiResult<AlipayTradeAppPayArgsRespDTO>> requestAlipayArgs(@Body AlipayTradeAppPayArgsReqDTO reqDTO) {
        return walletApi.requestAlipayArgs(reqDTO);
    }

    @Override
    public Observable<ApiResult<AlipayTradeAppPayResultParseRespDTO>> parseAlipayResule(@Body AlipayTradeAppPayResultParseReqDTO reqDTO) {
        return walletApi.parseAlipayResule(reqDTO);
    }

    @Override
    public Observable<ApiResult<QueryUserThirdAccountRespDTO>> requestThirdAccounts(@Body QueryUserThirdAccountReqDTO reqDTO) {
        return walletApi.requestThirdAccounts(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> addAccount(@Body AddThirdAccountReqDTO reqDTO) {
        return walletApi.addAccount(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> deleteAccount(SimpleReqDTO reqDTO) {
        return walletApi.deleteAccount(reqDTO);
    }

    @Override
    public Observable<ApiResult<FundsDTO>> queryWithdrawRechargeDetail(@Body SimpleReqDTO reqDTO) {
        return walletApi.queryWithdrawRechargeDetail(reqDTO);
    }
}
