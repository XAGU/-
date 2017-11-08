package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 充值详情
 * <p>
 * Created by zcd on 10/23/17.
 */

public interface IRechargeDetailPresenter<V extends IRechargeDetailView> extends IBasePresenter<V> {
    void requestData(Long id);

    String getToken();

    void remind(Long id);
}
