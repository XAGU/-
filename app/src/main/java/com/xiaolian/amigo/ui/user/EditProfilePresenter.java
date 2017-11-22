package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.user.User;
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
        super();
        this.manager = manager;
    }

    @Override
    public void getPersonProfile() {
        addObserver(manager.getUserInfo(), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    getMvpView().setAvatar(result.getData().getPictureUrl());
                    getMvpView().setMobile(result.getData().getMobile());
                    getMvpView().setNickName(result.getData().getNickName());
                    getMvpView().setSchoolName(result.getData().getSchoolName());
                    getMvpView().setResidenceName(result.getData().getResidenceName());
                    if (result.getData().getSex() != null) {
                        getMvpView().setSex(result.getData().getSex());
                    }
                    User user = new User(result.getData());
                    manager.setUser(user);
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void checkChangeSchool() {
        addObserver(manager.changeSchoolCheck(), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().showChangeSchoolDialog();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

}
