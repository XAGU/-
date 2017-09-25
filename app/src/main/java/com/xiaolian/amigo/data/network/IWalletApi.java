package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryTimeValidReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalWalletDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryRechargeAmountsRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryTimeValidRespDTO;

import rx.Observable;
import retrofit2.http.Body;
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

    // 获取金额列表
    @POST("/funds/recharge/amount/list")
    Observable<ApiResult<QueryRechargeAmountsRespDTO>> queryRechargeAmountList(@Body SimpleQueryReqDTO body);

    // 查询提现时间段
    @POST("/time/range/withdraw")
    Observable<ApiResult<QueryTimeValidRespDTO>> queryWithDrawTimeValid();
}
