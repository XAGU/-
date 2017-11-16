package com.xiaolian.amigo.ui.user;

import android.text.TextUtils;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.BuildingType;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.ResidenceLevel;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.BindResidenceReq;
import com.xiaolian.amigo.data.network.model.dto.request.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryResidenceListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryBriefSchoolListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.UserResidenceInListDTO;
import com.xiaolian.amigo.data.network.model.user.Residence;
import com.xiaolian.amigo.data.network.model.user.School;
import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.user.adaptor.ListChooseAdaptor;
import com.xiaolian.amigo.ui.user.intf.IListChoosePresenter;
import com.xiaolian.amigo.ui.user.intf.IListChooseView;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * <p>
 * Created by zcd on 9/19/17.
 */

public class ListChoosePresenter<V extends IListChooseView> extends BasePresenter<V>
        implements IListChoosePresenter<V> {

    private static final String TAG = ListChoosePresenter.class.getSimpleName();
    private IUserDataManager manager;

    @Inject
    public ListChoosePresenter(IUserDataManager manager) {
        super();
        this.manager = manager;
    }


    @Override
    public void getSchoolList(Integer page, Integer size) {
        SimpleQueryReqDTO dto = new SimpleQueryReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        addObserver(manager.getSchoolList(dto), new NetworkObserver<ApiResult<QueryBriefSchoolListRespDTO>>() {

            @Override
            public void onReady(ApiResult<QueryBriefSchoolListRespDTO> result) {
                if (null == result.getError()) {
                    if (null != result.getData().getSchools() && result.getData().getSchools().size() > 0) {
                        ArrayList<ListChooseAdaptor.Item> schoolWapper = new ArrayList<>();
                        for (School school : result.getData().getSchools()) {
                            schoolWapper.add(new ListChooseAdaptor.Item(school,
                                    CommonUtil.equals(school.getId(), manager.getUser().getSchoolId())));
                        }
                        getMvpView().addMore(schoolWapper);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getBuildList(Integer page, Integer size, Integer deviceType) {
        QueryResidenceListReqDTO dto = new QueryResidenceListReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        if (deviceType == Device.HEATER.getType()) {
            dto.setBuildingType(BuildingType.DORMITORY.getType());
        }
        dto.setDeviceType(deviceType);
        dto.setSchoolId(manager.getUser().getSchoolId());
        // residencelevel 1 表示楼栋
        dto.setResidenceLevel(ResidenceLevel.BUILDING.getType());
        addObserver(manager.queryResidenceList(dto), new NetworkObserver<ApiResult<ResidenceListRespDTO>>() {

            @Override
            public void onReady(ApiResult<ResidenceListRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getResidences() != null && result.getData().getResidences().size() > 0) {
                        ArrayList<ListChooseAdaptor.Item> wapper = new ArrayList<>();
                        for (Residence residence : result.getData().getResidences()) {
                            wapper.add(new ListChooseAdaptor.Item(residence));
                        }
                        getMvpView().addMore(wapper);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void updateSchool(Long schoolId) {
        PersonalUpdateReqDTO dto = new PersonalUpdateReqDTO();
        dto.setSchoolId(schoolId);
        addObserver(manager.updateUserInfo(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess(R.string.change_school_success);
                    getMvpView().backToMain();
                    manager.logout();
                    getMvpView().redirectToLogin();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void updateSex(int sex) {
        PersonalUpdateReqDTO dto = new PersonalUpdateReqDTO();
        dto.setSex(sex);
        addObserver(manager.updateUserInfo(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess(R.string.change_sex_success);
                    getMvpView().finishView();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });

    }

    @Override
    public void getFloorList(Integer page, Integer size, Integer deviceType, Long parentId) {
        QueryResidenceListReqDTO dto = new QueryResidenceListReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        dto.setParentId(parentId);
        dto.setDeviceType(deviceType);
        dto.setSchoolId(manager.getUser().getSchoolId());
        // residencelevel 2 表示楼层
        dto.setResidenceLevel(ResidenceLevel.FLOOR.getType());
        addObserver(manager.queryResidenceList(dto), new NetworkObserver<ApiResult<ResidenceListRespDTO>>() {

            @Override
            public void onReady(ApiResult<ResidenceListRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getResidences() != null && result.getData().getResidences().size() > 0) {
                        ArrayList<ListChooseAdaptor.Item> wapper = new ArrayList<>();
                        for (Residence residence : result.getData().getResidences()) {
                            wapper.add(new ListChooseAdaptor.Item(residence));
                        }
                        getMvpView().addMore(wapper);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getDormitoryList(Integer page, Integer size, Integer deviceType, Long parentId, boolean existDevice) {
        QueryResidenceListReqDTO dto = new QueryResidenceListReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        dto.setExistDevice(existDevice);
        dto.setDeviceType(deviceType);
        dto.setParentId(parentId);
        dto.setSchoolId(manager.getUser().getSchoolId());
        // residencelevel 3 表示宿舍
        dto.setResidenceLevel(ResidenceLevel.ROOM.getType());
        addObserver(manager.queryResidenceList(dto), new NetworkObserver<ApiResult<ResidenceListRespDTO>>() {

            @Override
            public void onReady(ApiResult<ResidenceListRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getResidences() != null && result.getData().getResidences().size() > 0) {
                        ArrayList<ListChooseAdaptor.Item> wapper = new ArrayList<>();
                        for (Residence residence : result.getData().getResidences()) {
                            wapper.add(new ListChooseAdaptor.Item(residence));
                        }
                        getMvpView().addMore(wapper);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }



    @Override
    public void bindDormitory(Long id, Long residenceId, boolean isEdit) {
        bindDormitory(id, residenceId, isEdit, null);
    }

    @Override
    public void bindDormitory(Long id, Long residenceId, boolean isEdit, String activitySrc) {
        BindResidenceReq dto = new BindResidenceReq();
        dto.setResidenceId(residenceId);
        if (isEdit) {
            dto.setId(id);
        }
        addObserver(manager.bindResidence(dto), new NetworkObserver<ApiResult<UserResidenceInListDTO>>() {

            @Override
            public void onReady(ApiResult<UserResidenceInListDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess(R.string.add_dormitory_success);
                    User user = manager.getUser();
                    user.setResidenceName(result.getData().getResidenceName());
                    user.setResidenceId(result.getData().getResidenceId());
                    manager.setUser(user);
                    if (TextUtils.equals(activitySrc, Constant.EDIT_PROFILE_ACTIVITY_SRC)) {
                        getMvpView().backToEditDormitory();
                    } else if (TextUtils.equals(activitySrc, Constant.MAIN_ACTIVITY_SRC)) {
                        getMvpView().backToMain(activitySrc);
                    } else {
                        getMvpView().backToDormitory();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
