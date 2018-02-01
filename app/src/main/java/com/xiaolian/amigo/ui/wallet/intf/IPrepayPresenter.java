package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 预付金额
 *
 * @author zcd
 * @author 17/10/10
 */

public interface IPrepayPresenter<V extends IPrepayView> extends IBasePresenter<V> {

    /**
     * 查看预付金额
     *
     * @param page 页数
     */
    void requestPrepay(int page);
}
