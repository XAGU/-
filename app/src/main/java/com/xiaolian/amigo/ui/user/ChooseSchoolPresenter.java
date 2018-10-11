package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.user.SchoolNameListRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolPresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolView;

import javax.inject.Inject;

public class ChooseSchoolPresenter<v extends IChooseSchoolView> extends BasePresenter<v>
     implements IChooseSchoolPresenter<v>{


    private IUserDataManager userDataManager ;

    @Inject
    public ChooseSchoolPresenter(IUserDataManager userDataManager){
        this.userDataManager = userDataManager ;
    }

    @Override
    public void getSchoolNameList() {
        addObserver(userDataManager.getSchoolNameList(), new NetworkObserver<ApiResult<SchoolNameListRespDTO>>() {

            @Override
            public void onReady(ApiResult<SchoolNameListRespDTO> result) {
                getMvpView().setreferComplete();
                if (result.getError() == null) {
                    getMvpView().referSchoolList(result.getData().getSchoolNameList());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().setreferComplete();
            }
        });
    }
}
