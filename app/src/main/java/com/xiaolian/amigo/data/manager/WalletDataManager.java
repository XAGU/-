package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.IAlipayApi;
import com.xiaolian.amigo.data.network.IComplaintApi;
import com.xiaolian.amigo.data.network.ICsApi;
import com.xiaolian.amigo.data.network.IFundsApi;
import com.xiaolian.amigo.data.network.ITimeRangeApi;
import com.xiaolian.amigo.data.network.IUserBillApi;
import com.xiaolian.amigo.data.network.IUserThirdAccountApi;
import com.xiaolian.amigo.data.network.IWxpayApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.complaint.CheckComplaintReqDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryRechargeTypeListRespDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryRechargeTypesRespDTO;
import com.xiaolian.amigo.data.network.model.funds.SchoolWechatAccountRespDTO;
import com.xiaolian.amigo.data.network.model.funds.WechatWithdrawReqDTO;
import com.xiaolian.amigo.data.network.model.funds.WithdrawExplanationRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.userbill.QueryMonthlyBillReqDTO;
import com.xiaolian.amigo.data.network.model.userbill.QueryPersonalMaxConsumeOrderListReqDTO;
import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillRespDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.AddThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayArgsReqDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayResultParseReqDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryPersonalFundsListReqDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.QueryUserThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.funds.RechargeReqDTO;
import com.xiaolian.amigo.data.network.model.cs.RemindReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.funds.WithdrawReqDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayArgsRespDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayResultParseRespDTO;
import com.xiaolian.amigo.data.network.model.funds.FundsDTO;
import com.xiaolian.amigo.data.network.model.funds.PersonalWalletDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryFundsListRespDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryRechargeAmountsRespDTO;
import com.xiaolian.amigo.data.network.model.timerange.QueryTimeValidRespDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.QueryUserThirdAccountRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayArgsReqDTO;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayArgsRespDTO;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayResultParseReqDTO;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayResultParseRespDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.di.UserServer;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.Body;
import rx.Observable;

/**
 * 我的钱包
 *
 * @author zcd
 * @date 17/9/18
 */

public class WalletDataManager implements IWalletDataManager {
    @SuppressWarnings("unused")
    private static final String TAG = WalletDataManager.class.getSimpleName();

    private ISharedPreferencesHelp sharedPreferencesHelp;
    private IAlipayApi alipayApi;
    private IWxpayApi wxpayApi;
    private ICsApi csApi;
    private IFundsApi fundsApi;
    private ITimeRangeApi timeRangeApi;
    private IUserThirdAccountApi userThirdAccountApi;
    private IComplaintApi complaintApi;
    private IUserBillApi userBillApi;

    @Inject
    public WalletDataManager(@UserServer Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        alipayApi = retrofit.create(IAlipayApi.class);
        wxpayApi = retrofit.create(IWxpayApi.class);
        csApi = retrofit.create(ICsApi.class);
        fundsApi = retrofit.create(IFundsApi.class);
        timeRangeApi = retrofit.create(ITimeRangeApi.class);
        userThirdAccountApi = retrofit.create(IUserThirdAccountApi.class);
        complaintApi = retrofit.create(IComplaintApi.class);
        userBillApi = retrofit.create(IUserBillApi.class);
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public Observable<ApiResult<PersonalWalletDTO>> queryWallet() {
        return fundsApi.queryWallet();
    }

    @Override
    public Observable<ApiResult<QueryRechargeAmountsRespDTO>> queryRechargeAmountList(@Body SimpleQueryReqDTO body) {
        return fundsApi.queryRechargeAmountList(body);
    }

    @Override
    public Observable<ApiResult<QueryRechargeTypesRespDTO>> queryRechargeTypes() {
        return fundsApi.queryRechargeTypes();
    }

    @Override
    public Observable<ApiResult<QueryTimeValidRespDTO>> queryWithDrawTimeValid() {
        return timeRangeApi.queryWithDrawTimeValid();
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> recharge(@Body RechargeReqDTO reqDTO) {
        return fundsApi.recharge(reqDTO);
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> withdraw(@Body WithdrawReqDTO reqDTO) {
        return fundsApi.withdraw(reqDTO);
    }

    @Override
    public Observable<ApiResult<QueryFundsListRespDTO>> queryWithdrawList(@Body QueryPersonalFundsListReqDTO reqDTO) {
        return fundsApi.queryWithdrawList(reqDTO);
    }

    @Override
    public Observable<ApiResult<AlipayTradeAppPayArgsRespDTO>> requestAlipayArgs(@Body AlipayTradeAppPayArgsReqDTO reqDTO) {
        return alipayApi.requestAlipayArgs(reqDTO);
    }

    @Override
    public Observable<ApiResult<WxpayTradeAppPayArgsRespDTO>> requestWxpayArgs(WxpayTradeAppPayArgsReqDTO reqDTO) {
        return wxpayApi.requestWxpayArgs(reqDTO);
    }

    @Override
    public Observable<ApiResult<AlipayTradeAppPayResultParseRespDTO>> parseAlipayResule(@Body AlipayTradeAppPayResultParseReqDTO reqDTO) {
        return alipayApi.parseAlipayResule(reqDTO);
    }

    @Override
    public Observable<ApiResult<WxpayTradeAppPayResultParseRespDTO>> parseWxpayResule(WxpayTradeAppPayResultParseReqDTO reqDTO) {
        return wxpayApi.parseWxpayResule(reqDTO);
    }

    @Override
    public Observable<ApiResult<QueryUserThirdAccountRespDTO>> requestThirdAccounts(@Body QueryUserThirdAccountReqDTO reqDTO) {
        return userThirdAccountApi.requestThirdAccounts(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> addAccount(@Body AddThirdAccountReqDTO reqDTO) {
        return userThirdAccountApi.addAccount(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> deleteAccount(SimpleReqDTO reqDTO) {
        return userThirdAccountApi.deleteAccount(reqDTO);
    }

    @Override
    public Observable<ApiResult<FundsDTO>> queryWithdrawRechargeDetail(@Body SimpleReqDTO reqDTO) {
        return fundsApi.queryWithdrawRechargeDetail(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> remind(RemindReqDTO reqDTO) {
        return csApi.remind(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> cancelWithdraw(SimpleReqDTO reqDTO) {
        return fundsApi.cancelWithdraw(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> checkComplaint(CheckComplaintReqDTO reqDTO) {
        return complaintApi.checkComplaint(reqDTO);
    }

    @Override
    public String getMobile() {
        return sharedPreferencesHelp.getUserInfo().getMobile();
    }

    @Override
    public Observable<ApiResult<UserMonthlyBillRespDTO>> getMonthlyBill(QueryMonthlyBillReqDTO reqDTO) {
        return userBillApi.getMonthlyBill(reqDTO);
    }

    @Override
    public Observable<ApiResult<OrderRespDTO>> getMonthlyMaxBill(QueryPersonalMaxConsumeOrderListReqDTO reqDTO) {
        return userBillApi.getMonthlyMaxBill(reqDTO);
    }

    @Override
    public User getUser() {
        return sharedPreferencesHelp.getUserInfo();
    }

    @Override
    public Observable<ApiResult<WithdrawExplanationRespDTO>> withDrawExplanation() {
        return fundsApi.withDrawExplanation();
    }

    @Override
    public boolean isShowWithDrawDialog() {
        return sharedPreferencesHelp.isShowWithDrawDialog();
    }

    @Override
    public void setIsShowWithDrawDialog(boolean b) {
        sharedPreferencesHelp.setIsShowWithDrawDialog(b);
    }

    @Override
    public int getUserCertification() {
        return sharedPreferencesHelp.getCertifyStatus();
    }

    @Override
    public Observable<ApiResult<SchoolWechatAccountRespDTO>> wechatAccountInfoAppid() {
        return fundsApi.wechatAccountInfoAppid();
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> wechatWithdraw(WechatWithdrawReqDTO reqDTO) {
        return fundsApi.wechatWithdraw(reqDTO);
    }

    @Override
    public Observable<ApiResult<QueryRechargeTypeListRespDTO>> typeList() {
        return fundsApi.typeList();
    }

    @Override
    public void setLastWithdrawId(Long id) {
        sharedPreferencesHelp.setLastWithdrawId(id);
    }

    @Override
    public Long getLastWithdrawId() {
        return sharedPreferencesHelp.getLastWithdrawId();
    }

    @Override
    public void setLastWithdrawName(String name) {
        sharedPreferencesHelp.setLastWithdrawName(name);
    }

    @Override
    public String getLastWithdrawName() {
        return sharedPreferencesHelp.getLastWithdrawName();
    }

    @Override
    public String getLastRechargeAmount() {
        return sharedPreferencesHelp.getLastRechargeAmount();
    }

    @Override
    public void setLastRechargeAmount(String amount) {
        sharedPreferencesHelp.setLastRechargeAmount(amount);
    }
}
