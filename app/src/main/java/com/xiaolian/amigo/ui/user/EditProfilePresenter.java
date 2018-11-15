package com.xiaolian.amigo.ui.user;

import android.util.Log;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.ApplySchoolCheckRespDTO;
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
                    Log.e(TAG, "onReady: --"+ result.getError().getDisplayMessage() );
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e(TAG, "onError: --"+ e.getMessage() );
            }
        });
    }

    @Override
    public void checkChangeSchool() {
        addObserver(userDataManager.applySchoolCheck(), new NetworkObserver<ApiResult<ApplySchoolCheckRespDTO>>() {

            @Override
            public void onReady(ApiResult<ApplySchoolCheckRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isHasApply()) {
                        //提交过学校审核，直接进入审核页面
                        getMvpView().gotoChangeSchool(result.getData());

                    } else {
                        //没有提交过更改学校审核, 进入选择学校界面
                        getMvpView().gotoChooseSchool();
                    }

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

    @Override
    public int getCertificationStatus() {
       return  userDataManager.getCertifyStatus();
    }

}
