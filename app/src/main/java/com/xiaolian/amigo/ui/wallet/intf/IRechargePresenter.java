package com.xiaolian.amigo.ui.wallet.intf;

import android.widget.Button;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 充值
 *
 * @author zcd
 * @date 17/9/20
 */

public interface IRechargePresenter<V extends IRechargeView> extends IBasePresenter<V> {
    /**
     * 获取充值列表
     */
    void getRechargeList();

    /**
     * 充值
     *
     * @param amount 金额
     * @param type   类型
     */
    void recharge(Double amount, int type , Button button);

    /**
     * 解析支付结果
     *
     * @param resultStatus 支付状态
     * @param result       支付结果
     * @param memo         支付记录
     */
    void parseAlipayResult(String resultStatus, String result, String memo);

    /**
     * 获取充值类型列表
     */
    void getRechargeTypeList();

    void parseWxpayResult(Integer wxResult);

    void getWithDrawExplain();

}
