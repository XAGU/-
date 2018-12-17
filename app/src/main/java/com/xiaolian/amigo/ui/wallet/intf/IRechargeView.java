package com.xiaolian.amigo.ui.wallet.intf;

import android.widget.Button;

import com.xiaolian.amigo.data.network.model.funds.WithdrawExplanationRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.wallet.adaptor.RechargeAdaptor;
import com.xiaolian.amigo.ui.wallet.adaptor.RechargeTypeAdaptor;
import com.xiaolian.amigo.util.PayUtil;

import java.util.List;

/**
 * 充值
 *
 * @author zcd
 * @date 17/9/20
 */

public interface IRechargeView extends IBaseView {
    /**
     * 加载支付列表
     *
     * @param rechargeWrappers 支付信息
     */
    void addMore(List<RechargeAdaptor.RechargeWrapper> rechargeWrappers);

    void setRechargeType(List<RechargeTypeAdaptor.RechargeWrapper> rechargeTypes);

    /**
     * 返回
     */
    void back();

    /**
     * 支付宝支付
     *
     * @param reqArgs 请求参数
     */
    void alipay(String reqArgs );

    void wxpay(PayUtil.IWeChatPayReq req);

    /**
     * 跳转到充值详情页面
     *
     * @param fundsId 账单id
     */
    void gotoDetail(Long fundsId);

    /**
     * 充值按钮可点击
     */
    void enableRecharge();

    void showWithDrawDialog(WithdrawExplanationRespDTO dto);
}
