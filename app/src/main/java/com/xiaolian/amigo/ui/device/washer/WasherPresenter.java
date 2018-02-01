package com.xiaolian.amigo.ui.device.washer;

import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IWasherPresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IWasherView;

import javax.inject.Inject;

/**
 * 洗衣机首页
 *
 * @author zcd
 * @date 18/1/12
 */

public class WasherPresenter<V extends IWasherView> extends BasePresenter<V>
        implements IWasherPresenter<V> {
    @Inject
    WasherPresenter() {
    }
}
