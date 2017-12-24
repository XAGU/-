package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryPersonalFundsListReqDTO;
import com.xiaolian.amigo.data.network.model.funds.RechargeReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.funds.WithdrawReqDTO;
import com.xiaolian.amigo.data.network.model.funds.FundsDTO;
import com.xiaolian.amigo.data.network.model.funds.PersonalWalletDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryFundsListRespDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryRechargeAmountsRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 资金相关api
 * <p>
 * Created by zcd on 17/12/14.
 */

public interface IFundsApi {
    // 获取余额
    @POST("funds/wallet/personal/one")
    Observable<ApiResult<PersonalWalletDTO>> queryWallet();

    // 获取金额列表
    @POST("funds/recharge/amount/list")
    Observable<ApiResult<QueryRechargeAmountsRespDTO>> queryRechargeAmountList(@Body SimpleQueryReqDTO body);

    // 提现
    @POST("funds/withdraw")
    Observable<ApiResult<SimpleRespDTO>> withdraw(@Body WithdrawReqDTO reqDTO);

    // 用户个人充值提现记录列表
    @POST("funds/personal/list")
    Observable<ApiResult<QueryFundsListRespDTO>> queryWithdrawList(@Body QueryPersonalFundsListReqDTO reqDTO);

    // 充值
    @POST("funds/recharge")
    Observable<ApiResult<SimpleRespDTO>> recharge(@Body RechargeReqDTO reqDTO);

    // 充值提现
    @POST("funds/one")
    Observable<ApiResult<FundsDTO>> queryWithdrawRechargeDetail(@Body SimpleReqDTO reqDTO);

    // 取消充值
    @POST("funds/withdraw/cancel")
    Observable<ApiResult<BooleanRespDTO>> cancelWithdraw(@Body SimpleReqDTO reqDTO);

}
