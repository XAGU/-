package com.xiaolian.amigo.ui.device.bathroom;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IPayUsePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IPayUseView;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/6/29
 */
public class PayUsePresenter<V extends IPayUseView> extends BasePresenter<V>
        implements IPayUsePresenter<V> {
    private IBathroomDataManager bathroomDataManager;

    @Inject
    public PayUsePresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }
}
