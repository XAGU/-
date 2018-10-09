package com.xiaolian.amigo.ui.device.bathroom;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyRecordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyRecordView;

import javax.inject.Inject;

/**
 * 购买记录
 *
 * @author zcd
 * @date 18/6/29
 */
public class BuyRecordPresenter<V extends IBuyRecordView> extends BasePresenter<V>
        implements IBuyRecordPresenter<V> {
    private IBathroomDataManager bathroomDataManager;

    @Inject
    public BuyRecordPresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }

    @Override
    public void getBuyRecordList() {

    }
}
