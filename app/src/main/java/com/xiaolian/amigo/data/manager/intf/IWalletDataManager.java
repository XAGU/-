package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.complaint.CheckComplaintReqDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.AddThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayArgsReqDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayResultParseReqDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryPersonalFundsListReqDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.QueryUserThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.funds.RechargeReqDTO;
import com.xiaolian.amigo.data.network.model.cs.RemindReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.funds.WithdrawReqDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayArgsRespDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayResultParseRespDTO;
import com.xiaolian.amigo.data.network.model.funds.FundsDTO;
import com.xiaolian.amigo.data.network.model.funds.PersonalWalletDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryFundsListRespDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryRechargeAmountsRespDTO;
import com.xiaolian.amigo.data.network.model.timerange.QueryTimeValidRespDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.QueryUserThirdAccountRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;

import retrofit2.http.Body;
import rx.Observable;

/**
 * 我的钱包
 * <p>
 * Created by zcd on 9/18/17.
 */

public interface IWalletDataManager {
    void setLastWithdrawId(Long id);

    Long getLastWithdrawId();

    void setLastWithdrawName(String name);

    String getLastWithdrawName();

    String getLastRechargeAmount();

    void setLastRechargeAmount(String amount);

    // 查询提现时间段
    Observable<ApiResult<QueryTimeValidRespDTO>> queryWithDrawTimeValid();

    // 获取支付宝app支付订单请求参数
    Observable<ApiResult<AlipayTradeAppPayArgsRespDTO>> requestAlipayArgs(@Body AlipayTradeAppPayArgsReqDTO reqDTO);

    // 解析app支付j结果
    Observable<ApiResult<AlipayTradeAppPayResultParseRespDTO>> parseAlipayResule(@Body AlipayTradeAppPayResultParseReqDTO reqDTO);

    // 第三方账号列表
    Observable<ApiResult<QueryUserThirdAccountRespDTO>> requestThirdAccounts(@Body QueryUserThirdAccountReqDTO reqDTO);

    // 新增第三方账号
    Observable<ApiResult<BooleanRespDTO>> addAccount(@Body AddThirdAccountReqDTO reqDTO);

    // 删除第三方账户
    Observable<ApiResult<BooleanRespDTO>> deleteAccount(@Body SimpleReqDTO reqDTO);

    // 提醒客服
    Observable<ApiResult<BooleanRespDTO>> remind(@Body RemindReqDTO reqDTO);

    // 获取余额
    Observable<ApiResult<PersonalWalletDTO>> queryWallet();

    // 获取金额列表
    Observable<ApiResult<QueryRechargeAmountsRespDTO>> queryRechargeAmountList(@Body SimpleQueryReqDTO body);

    // 提现
    Observable<ApiResult<SimpleRespDTO>> withdraw(@Body WithdrawReqDTO reqDTO);

    // 用户个人充值提现记录列表
    Observable<ApiResult<QueryFundsListRespDTO>> queryWithdrawList(@Body QueryPersonalFundsListReqDTO reqDTO);

    // 充值
    Observable<ApiResult<SimpleRespDTO>> recharge(@Body RechargeReqDTO reqDTO);

    // 充值提现
    Observable<ApiResult<FundsDTO>> queryWithdrawRechargeDetail(@Body SimpleReqDTO reqDTO);

    // 取消充值
    Observable<ApiResult<BooleanRespDTO>> cancelWithdraw(@Body SimpleReqDTO reqDTO);

    // 投诉查重
    Observable<ApiResult<BooleanRespDTO>> checkComplaint(@Body CheckComplaintReqDTO reqDTO);
}
