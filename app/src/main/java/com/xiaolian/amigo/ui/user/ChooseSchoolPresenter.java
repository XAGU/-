package com.xiaolian.amigo.ui.user;

import android.util.Log;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.CheckSchoolRespDTO;
import com.xiaolian.amigo.data.network.model.login.ClearTokenReqDTO;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.school.QueryBriefSchoolListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolListReqDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.SchoolNameListRespDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolPresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolView;
import com.xiaolian.amigo.ui.widget.school.mode.CityBean;

import javax.inject.Inject;

import static com.xiaolian.amigo.ui.user.ListChooseActivity.ACTION_LIST_SCHOOL_RESULT;

public class ChooseSchoolPresenter<v extends IChooseSchoolView> extends BasePresenter<v>
        implements IChooseSchoolPresenter<v> {


    private IUserDataManager userDataManager;

    private int actionId;


    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    @Inject
    public ChooseSchoolPresenter(IUserDataManager userDataManager) {
        this.userDataManager = userDataManager;
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
    public void updataSchool(CityBean cityBean) {
        if (actionId == ACTION_LIST_SCHOOL_RESULT) {
            getMvpView().finishResult(cityBean);
        } else {
            PersonalUpdateReqDTO personalUpdateReqDTO = new PersonalUpdateReqDTO();
            personalUpdateReqDTO.setSchoolId(cityBean.getId());
            addObserver(userDataManager.updateUserInfo(personalUpdateReqDTO), new NetworkObserver<ApiResult<EntireUserDTO>>() {
                @Override
                public void onReady(ApiResult<EntireUserDTO> result) {
                    if (result.getError() == null) {
                        User user = new User(result.getData());
                        userDataManager.setUser(user);
                        getMvpView().backToProfile();
                    } else {
                        getMvpView().onError(result.getError().getDisplayMessage());
                    }
                }
            });
        }
    }

    @Override
    public void checkSchool(CityBean cityBean) {
        if (actionId == ACTION_LIST_SCHOOL_RESULT) {
            getMvpView().finishResult(cityBean);
        } else {
            addObserver(userDataManager.changeSchoolCheck(), new NetworkObserver<ApiResult<CheckSchoolRespDTO>>() {

                @Override
                public void onReady(ApiResult<CheckSchoolRespDTO> result) {
                    if (null == result.getError()) {
                        //没有消费过，满足条件可以直接请求切换学校
                        if (result.getData().isResult()) {
                            getMvpView().showChangeSchoolDialog();
                        } else {
                            //有消费过，不满足条件进入申请更换学校页面
                            getMvpView().requestChangeShool();
                        }
                    } else {
                        getMvpView().onError(result.getError().getDisplayMessage());
                        Log.e(TAG, "onReady: " + result.getError().getDisplayMessage());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    getMvpView().onError("网络错误");
                }
            });
        }
    }

    @Override
    public void clearToken(CityBean cityBean) {
        ClearTokenReqDTO reqDTO = new ClearTokenReqDTO();
        reqDTO.setId(cityBean.getId());

        addObserver(userDataManager.clearToken(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {
            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()){
                        getMvpView().onSuccess(R.string.change_school_success);
                        getMvpView().backToProfile();
                    }else{

                    }
                }else{

                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().onError("网络异常");
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
            public void onStart() {

            }

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
    @Override
    public long getCurentSchoolId(){
       return userDataManager.getUser().getSchoolId();
    }
}
