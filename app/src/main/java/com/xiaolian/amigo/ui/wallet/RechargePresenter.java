package com.xiaolian.amigo.ui.wallet;

import android.text.TextUtils;

import com.xiaolian.amigo.data.enumeration.AlipayPayOrderCheckResult;
import com.xiaolian.amigo.data.enumeration.PayWay;
import com.xiaolian.amigo.data.enumeration.WxpayPayOrderCheckResult;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayArgsReqDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayResultParseReqDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryRechargeTypesRespDTO;
import com.xiaolian.amigo.data.network.model.funds.RechargeReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayArgsRespDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayResultParseRespDTO;
import com.xiaolian.amigo.data.network.model.funds.QueryRechargeAmountsRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.wallet.RechargeDenominations;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayArgsReqDTO;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayArgsRespDTO;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayResultParseReqDTO;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayResultParseRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.adaptor.RechargeAdaptor;
import com.xiaolian.amigo.ui.wallet.adaptor.RechargeTypeAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IRechargePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeView;
import com.xiaolian.amigo.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * 充值
 *
 * @author zcd
 * @date 17/9/20
 */

public class RechargePresenter<V extends IRechargeView> extends BasePresenter<V>
        implements IRechargePresenter<V> {
    private static final String TAG = RechargePresenter.class.getSimpleName();
    private IWalletDataManager manager;
    private Long fundsId;

    @Inject
    RechargePresenter(IWalletDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void getRechargeList() {
        addObserver(manager.queryRechargeAmountList(new SimpleQueryReqDTO()), new NetworkObserver<ApiResult<QueryRechargeAmountsRespDTO>>() {

            @Override
            public void onReady(ApiResult<QueryRechargeAmountsRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getRechargeDenominations() != null
                            && result.getData().getRechargeDenominations().size() > 0) {
                        // 上次选择的amount
                        String lastAmount = manager.getLastRechargeAmount();
                        List<RechargeAdaptor.RechargeWrapper> rechargeWrapper = new ArrayList<>();
                        for (RechargeDenominations rechargeDenominations : result.getData().getRechargeDenominations()) {
                            if (TextUtils.equals(lastAmount, String.format(Locale.getDefault(), "%.2f",
                                    rechargeDenominations.getAmount()))) {
                                rechargeWrapper.add(
                                        new RechargeAdaptor.RechargeWrapper(rechargeDenominations, true));
                                getMvpView().enableRecharge();
                            } else {
                                rechargeWrapper.add(
                                        new RechargeAdaptor.RechargeWrapper(rechargeDenominations));
                            }
                        }
                        getMvpView().addMore(rechargeWrapper);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void recharge(Double amount, int type) {
        String amountStr = String.format(Locale.getDefault(), "%.2f", amount);
        manager.setLastRechargeAmount(amountStr);
        Log.i(TAG, "储存充值amount: " + amountStr);
        RechargeReqDTO reqDTO = new RechargeReqDTO();
        reqDTO.setAmount(amount);
        reqDTO.setThirdAccountType(type);
        addObserver(manager.recharge(reqDTO), new NetworkObserver<ApiResult<SimpleRespDTO>>() {

            @Override
            public void onReady(ApiResult<SimpleRespDTO> result) {
                if (null == result.getError()) {
                    if (PayWay.getPayWay(type) == PayWay.ALIAPY) {
                        requestAlipayArgs(result.getData().getId());
                    } else if (PayWay.getPayWay(type) == PayWay.WECHAT) {
//                        getMvpView().onError("暂不支持微信支付");
                        requestWxpayArgs(result.getData().getId());
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    private void requestWxpayArgs(Long fundsId) {
        this.fundsId = fundsId;
        WxpayTradeAppPayArgsReqDTO reqDTO = new WxpayTradeAppPayArgsReqDTO();
        reqDTO.setFundsId(fundsId);
        addObserver(manager.requestWxpayArgs(reqDTO), new NetworkObserver<ApiResult<WxpayTradeAppPayArgsRespDTO>>() {
            @Override
            public void onReady(ApiResult<WxpayTradeAppPayArgsRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().wxpay(result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });

    }

    private void requestAlipayArgs(Long fundsId) {
        this.fundsId = fundsId;
        AlipayTradeAppPayArgsReqDTO reqDTO = new AlipayTradeAppPayArgsReqDTO();
        reqDTO.setFundsId(fundsId);
        addObserver(manager.requestAlipayArgs(reqDTO), new NetworkObserver<ApiResult<AlipayTradeAppPayArgsRespDTO>>() {

            @Override
            public void onReady(ApiResult<AlipayTradeAppPayArgsRespDTO> result) {
                if (null == result.getError()) {
                    Log.d(TAG, result.getData().getReqArgs());
                    getMvpView().alipay(result.getData().getReqArgs());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }

            }
        });
    }

    @Override
    public void parseAlipayResult(String resultStatus, String result, String memo) {
        AlipayTradeAppPayResultParseReqDTO reqDTO = new AlipayTradeAppPayResultParseReqDTO();
        reqDTO.setMemo(memo);
        reqDTO.setResult(result);
        reqDTO.setResultStatus(resultStatus);
        reqDTO.setFundsId(fundsId);
        addObserver(manager.parseAlipayResule(reqDTO), new NetworkObserver<ApiResult<AlipayTradeAppPayResultParseRespDTO>>() {

            @Override
            public void onReady(ApiResult<AlipayTradeAppPayResultParseRespDTO> apiResult) {
                if (apiResult.getError() == null) {
                    if (apiResult.getData().getCode() == AlipayPayOrderCheckResult.SUCCESS.getType()) {
                        getMvpView().onSuccess("充值成功");
                        getMvpView().gotoDetail(fundsId);
                    } else if (apiResult.getData().getCode() == AlipayPayOrderCheckResult.CANCEL.getType()) {
                        // TODO fix warning
                        // ignore cancel
                    } else {
                        getMvpView().onError(apiResult.getData().getMsg());
                    }
                } else {
                    getMvpView().onError(apiResult.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getRechargeTypeList() {
        addObserver(manager.queryRechargeTypes(), new NetworkObserver<ApiResult<QueryRechargeTypesRespDTO>>() {

            @Override
            public void onReady(ApiResult<QueryRechargeTypesRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getRechargeTypes() != null
                            && result.getData().getRechargeTypes().size() > 0) {
                        List<RechargeTypeAdaptor.RechargeWrapper> rechargeTypes = new ArrayList<>();
                        if (result.getData().getRechargeTypes().contains(PayWay.ALIAPY.getType())) {
                            rechargeTypes.add(new RechargeTypeAdaptor.RechargeWrapper(PayWay.ALIAPY.getType(), PayWay.ALIAPY.getDrawableRes(), "支付宝", true));
                        }
                        if (result.getData().getRechargeTypes().contains(PayWay.WECHAT.getType())) {
                            rechargeTypes.add(new RechargeTypeAdaptor.RechargeWrapper(PayWay.WECHAT.getType(), PayWay.WECHAT.getDrawableRes(), "微信", false));
                        }
                        getMvpView().setRechargeType(rechargeTypes);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void parseWxpayResult(Integer wxResult) {
        WxpayTradeAppPayResultParseReqDTO reqDTO = new WxpayTradeAppPayResultParseReqDTO();
        reqDTO.setResult(wxResult);
        reqDTO.setFundsId(fundsId);
        addObserver(manager.parseWxpayResule(reqDTO), new NetworkObserver<ApiResult<WxpayTradeAppPayResultParseRespDTO>>() {

            @Override
            public void onReady(ApiResult<WxpayTradeAppPayResultParseRespDTO> apiResult) {
                getMvpView().hideLoading();
                if (apiResult.getError() == null) {
                    if (apiResult.getData().getCode() == WxpayPayOrderCheckResult.SUCCESS.getType()) {
                        getMvpView().onSuccess("充值成功");
                        getMvpView().gotoDetail(fundsId);
                    } else if (apiResult.getData().getCode() == WxpayPayOrderCheckResult.CANCEL.getType()) {
                        // ignore cancel
                    } else {
                        getMvpView().onError(apiResult.getData().getMsg());
                    }
                } else {
                    getMvpView().onError(apiResult.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().hideLoading();
            }
        });
    }
}
