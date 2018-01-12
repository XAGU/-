package com.xiaolian.amigo.ui.washer;

import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.washer.intf.IWasherPresenter;
import com.xiaolian.amigo.ui.washer.intf.IWasherView;

import javax.inject.Inject;

/**
 * 洗衣机首页
 * <p>
 * Created by zcd on 18/1/12.
 */
public class WasherPresenter<V extends IWasherView> extends BasePresenter<V>
        implements IWasherPresenter<V> {
    @Inject
    public WasherPresenter() {
    }
}
