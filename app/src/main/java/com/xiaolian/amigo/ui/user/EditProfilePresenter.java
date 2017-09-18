package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfileView;

import javax.inject.Inject;

/**
 * EditProfilePresenter实现类
 * @author zcd
 */

public class EditProfilePresenter<V extends IEditProfileView> extends BasePresenter<V>
        implements IEditProfilePresenter<V> {

    private static final String TAG = EditProfilePresenter.class.getSimpleName();
    private IUserDataManager manager;

    @Inject
    public EditProfilePresenter(IUserDataManager manager) {
        this.manager = manager;
    }

    @Override
    public void getPersonProfile() {
        addObserver(manager.getUserInfo(), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> entireUserDTOApiResult) {

            }
        });
    }
}
