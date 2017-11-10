package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 提现详情
 * <p>
 * Created by zcd on 10/23/17.
 */

public interface IWithdrawalDetailPresenter<V extends IWithdrawalDetailView> extends IBasePresenter<V> {
    void requestData(Long id);

    String getToken();

    // 提醒客服
    void remind(Long sourceId);

    void cancelWithdraw(Long id);
}
