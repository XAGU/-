package com.xiaolian.amigo.ui.washer;

import com.xiaolian.amigo.data.manager.intf.IWasherDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.washer.intf.IChooseWashModePresenter;
import com.xiaolian.amigo.ui.washer.intf.IChooseWashModeView;

import javax.inject.Inject;

/**
 * 选择洗衣机模式
 * <p>
 * Created by zcd on 18/1/12.
 */

public class ChooseWashModePresenter<V extends IChooseWashModeView> extends BasePresenter<V>
        implements IChooseWashModePresenter<V> {
    private IWasherDataManager washerDataManager;

    @Inject
    public ChooseWashModePresenter(IWasherDataManager washerDataManager) {
        this.washerDataManager = washerDataManager;
    }
}
