package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 我的钱包
 *
 * @author zcd
 * @date 17/9/18
 */

public interface IWalletView extends IBaseView {
    /**
     * 设置余额
     *
     * @param balance 余额
     */
    void setBalanceText(Double balance);

    /**
     * 设置预付金额
     *
     * @param prepay 预付金额
     */
    void setPrepayText(Double prepay);

    /**
     * 跳转到提现页面
     */
    void gotoWithDraw();

    /**
     * 显示不在提现时间对话框
     *
     * @param title  标题
     * @param remark 备注
     */
    void showTimeValidDialog(String title, String remark);

    /**
     * 设置赠送金额
     */
    void setBalancePresentText(Double givingBalance);

    /**
     * 设置可提现余额
     */
    void setWithdrawAvailableText(Double chargeBalance);

    /**
     * 隐藏问号
     */
    void hideBalanceExplain();

    /**
     * 显示问号
     */
    void showBalanceExplain();
}
