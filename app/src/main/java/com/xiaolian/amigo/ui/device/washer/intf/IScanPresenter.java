package com.xiaolian.amigo.ui.device.washer.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * <p>
 * Created by zcd on 18/1/17.
 */

public interface IScanPresenter<V extends IScanView> extends IBasePresenter<V> {
    void scanCheckout(String content);
}
