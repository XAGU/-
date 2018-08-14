package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.ICompleteInfoPresenter;
import com.xiaolian.amigo.ui.user.intf.ICompleteInfoView;

import javax.inject.Inject;

public class CompleteInfoPresenter<V extends ICompleteInfoView>  extends BasePresenter<V>
             implements ICompleteInfoPresenter<V>{

    private IUserDataManager userDataManager;
    @Inject
    CompleteInfoPresenter(IUserDataManager userDataManager){
        this.userDataManager = userDataManager ;
    }

    @Override
    public User getUserInfo() {
        return userDataManager.getUser();
    }

    @Override
    public void updateSex(int sex) {
        PersonalUpdateReqDTO dto = new PersonalUpdateReqDTO();
        dto.setSex(sex);
        addObserver(userDataManager.updateUserInfo(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
//                    getMvpView().onSuccess(R.string.change_sex_success);
//                    getMvpView().finishView();
                } else {
//                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });

    }
}
