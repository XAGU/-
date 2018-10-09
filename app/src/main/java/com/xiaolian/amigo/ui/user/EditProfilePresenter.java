package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfileView;

import javax.inject.Inject;

/**
 * EditProfilePresenter实现类
 *
 * @author zcd
 * @date 17/9/15
 */

public class EditProfilePresenter<V extends IEditProfileView> extends BasePresenter<V>
        implements IEditProfilePresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = EditProfilePresenter.class.getSimpleName();
    private IUserDataManager userDataManager;
    private boolean isHadSetBathPassword;

    @Inject
    EditProfilePresenter(IUserDataManager userDataManager) {
        super();
        this.userDataManager = userDataManager;
    }

    @Override
    public void getPersonProfile() {
        addObserver(userDataManager.getUserInfo(), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    getMvpView().setAvatar(result.getData().getPictureUrl());
                    getMvpView().setMobile(result.getData().getMobile());
                    getMvpView().setNickName(result.getData().getNickName());
                    getMvpView().setSchoolName(result.getData().getSchoolName());
                    getMvpView().setResidenceName(result.getData().getResidenceName());
                    getMvpView().showBathroomPassword(userDataManager.isExistBathroomBiz(), result.getData().isHadSetBathPassword());
                    isHadSetBathPassword = result.getData().isHadSetBathPassword();
                    userDataManager.setBathroomPassword();
                    if (result.getData().getSex() != null) {
                        getMvpView().setSex(result.getData().getSex());
                    }
                    User user = new User(result.getData());
                    userDataManager.setUser(user);
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void checkChangeSchool() {
        addObserver(userDataManager.changeSchoolCheck(), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

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

    @Override
    public boolean isHadSetBathPassword() {
        return isHadSetBathPassword;
    }

}
