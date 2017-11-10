package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.AddThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.AlipayTradeAppPayArgsReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.AlipayTradeAppPayResultParseReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.CheckComplaintReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryPersonalFundsListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryUserThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RechargeReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RemindReqDTO;
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

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

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

    // 充值
    @POST("/funds/recharge")
    Observable<ApiResult<SimpleRespDTO>> recharge(@Body RechargeReqDTO reqDTO);

    // 提现
    @POST("/funds/withdraw")
    Observable<ApiResult<SimpleRespDTO>> withdraw(@Body WithdrawReqDTO reqDTO);

    // 用户个人充值提现记录列表
    @POST("/funds/personal/list")
    Observable<ApiResult<QueryFundsListRespDTO>> queryWithdrawList(@Body QueryPersonalFundsListReqDTO reqDTO);

    // 获取支付宝app支付订单请求参数
    @POST("/alipay/trade/app/pay/req/args")
    Observable<ApiResult<AlipayTradeAppPayArgsRespDTO>> requestAlipayArgs(@Body AlipayTradeAppPayArgsReqDTO reqDTO);

    // 解析app支付j结果
    @POST("/alipay/trade/app/pay/resp/result/parse")
    Observable<ApiResult<AlipayTradeAppPayResultParseRespDTO>> parseAlipayResule(@Body AlipayTradeAppPayResultParseReqDTO reqDTO);

    // 第三方账号列表
    @POST("/user/third/account/list")
    Observable<ApiResult<QueryUserThirdAccountRespDTO>> requestThirdAccounts(@Body QueryUserThirdAccountReqDTO reqDTO);

    // 新增第三方账号
    @POST("/user/third/account/add")
    Observable<ApiResult<BooleanRespDTO>> addAccount(@Body AddThirdAccountReqDTO reqDTO);

    // 删除第三方账户
    @POST("/user/third/account/delete")
    Observable<ApiResult<BooleanRespDTO>> deleteAccount(@Body SimpleReqDTO reqDTO);

    // 充值提现
    @POST("/funds/one")
    Observable<ApiResult<FundsDTO>> queryWithdrawRechargeDetail(@Body SimpleReqDTO reqDTO);

    // 提醒客服
    @POST("/cs/remind")
    Observable<ApiResult<BooleanRespDTO>> remind(@Body RemindReqDTO reqDTO);

    // 取消充值
    @POST("/funds/withdraw/cancel")
    Observable<ApiResult<BooleanRespDTO>> cancelWithdraw(@Body SimpleReqDTO reqDTO);

    // 投诉查重
    @POST("/complaint/check")
    Observable<ApiResult<BooleanRespDTO>> checkComplaint(@Body CheckComplaintReqDTO reqDTO);
}
