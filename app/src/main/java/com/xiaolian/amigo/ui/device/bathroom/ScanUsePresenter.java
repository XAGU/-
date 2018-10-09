package com.xiaolian.amigo.ui.device.bathroom;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IScanUsePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IScanUseView;

import javax.inject.Inject;

/**
 * 扫码使用
 *
 * @author zcd
 * @date 18/7/13
 */
public class ScanUsePresenter<V extends IScanUseView> extends BasePresenter<V>
        implements IScanUsePresenter<V> {
    private IBathroomDataManager bathroomDataManager;

    @Inject
    public ScanUsePresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }
}
