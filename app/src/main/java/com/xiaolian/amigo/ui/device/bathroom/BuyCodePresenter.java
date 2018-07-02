package com.xiaolian.amigo.ui.device.bathroom;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyCodeView;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyCodePresenter;

import javax.inject.Inject;

/**
 * 购买编码
 *
 * @author zcd
 * @date 18/6/29
 */
public class BuyCodePresenter<V extends IBuyCodeView> extends BasePresenter<V>
        implements IBuyCodePresenter<V> {
    private IBathroomDataManager bathroomDataManager;

    @Inject
    public BuyCodePresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }
}
