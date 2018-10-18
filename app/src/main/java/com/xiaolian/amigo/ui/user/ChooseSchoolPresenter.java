package com.xiaolian.amigo.ui.user;

import android.support.v4.util.ObjectsCompat;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.school.QueryBriefSchoolListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolListReqDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.School;
import com.xiaolian.amigo.data.network.model.user.SchoolNameListRespDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.adaptor.ListChooseAdaptor;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolPresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolView;

import java.util.ArrayList;

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

    @Override
    public void updataSchool(Long id) {
        PersonalUpdateReqDTO personalUpdateReqDTO = new PersonalUpdateReqDTO();
        personalUpdateReqDTO.setSchoolId(id);
        addObserver(userDataManager.updateUserInfo(personalUpdateReqDTO) ,new NetworkObserver<ApiResult<EntireUserDTO>>(){

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (result.getError() == null){
                    User user = new User(result.getData());
                    userDataManager.setUser(user);
                    getMvpView().backToProfile();
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getSchoolList(Integer page, Integer size, Boolean online) {
        QuerySchoolListReqDTO dto = new QuerySchoolListReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        dto.setOnline(online);
        addObserver(userDataManager.getSchoolList(dto), new NetworkObserver<ApiResult<QueryBriefSchoolListRespDTO>>() {

            @Override
            public void onReady(ApiResult<QueryBriefSchoolListRespDTO> result) {
                if (null == result.getError()) {
                    if (null != result.getData().getSchools() && result.getData().getSchools().size() > 0) {
                        getMvpView().showOnLineSchool(result.getData().getSchools());
                    } else {
                        getMvpView().onSuccess("没有学校");
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
