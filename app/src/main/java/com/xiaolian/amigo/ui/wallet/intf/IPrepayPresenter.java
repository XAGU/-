package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 预付金额
 * <p>
 * Created by zcd on 10/10/17.
 */

public interface IPrepayPresenter<V extends IPrepayView> extends IBasePresenter<V>{

    // 查看预付金额
    void requestPrepay(int page);
}
