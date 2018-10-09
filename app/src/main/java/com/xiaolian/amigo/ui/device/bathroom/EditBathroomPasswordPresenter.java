package com.xiaolian.amigo.ui.device.bathroom;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IEditBathroomPasswordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IEditBathroomPasswordView;

import javax.inject.Inject;

/**
 * 设置浴室密码
 *
 * @author zcd
 * @date 18/7/13
 */
public class EditBathroomPasswordPresenter<V extends IEditBathroomPasswordView>
        extends BasePresenter<V> implements IEditBathroomPasswordPresenter<V> {
    private IBathroomDataManager bathroomDataManager;

    @Inject
    public EditBathroomPasswordPresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }
}
