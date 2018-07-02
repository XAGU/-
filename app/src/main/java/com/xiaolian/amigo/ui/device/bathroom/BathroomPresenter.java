package com.xiaolian.amigo.ui.device.bathroom;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomView;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/6/29
 */
public class BathroomPresenter<V extends IBathroomView> extends BasePresenter<V>
    implements IBathroomPresenter<V> {
    private IBathroomDataManager bathroomDataManager;

    @Inject
    public BathroomPresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }
}
