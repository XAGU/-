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

    void showWithDraw();

    void hideWithDraw();
}
