package com.xiaolian.amigo.ui.washer;

import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.washer.intf.IWashPresenter;
import com.xiaolian.amigo.ui.washer.intf.IWashView;

import javax.inject.Inject;

/**
 * 洗衣机首页
 * <p>
 * Created by zcd on 18/1/12.
 */
public class WashPresenter<V extends IWashView> extends BasePresenter<V>
        implements IWashPresenter<V> {
    @Inject
    public WashPresenter() {
    }
}
