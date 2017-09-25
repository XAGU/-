package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 我的钱包
 * <p>
 * Created by zcd on 9/18/17.
 */

public interface IWalletView extends IBaseView {
    void setBalanceText(String balance);

    void setPrepayText(String prepay);

    void gotoWithDraw();

    void showTimeValidDialog(String title, String remark);
}
