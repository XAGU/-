package com.xiaolian.amigo.ui.device.bathroom;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomScanPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomScanView;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/7/13
 */
public class BathroomScanPresenter<V extends IBathroomScanView>
    extends BasePresenter<V> implements IBathroomScanPresenter<V> {
    private IBathroomDataManager bathroomDataManager;

    @Inject
    public BathroomScanPresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }
}
