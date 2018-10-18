package com.xiaolian.amigo.ui.user;

import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.BuildingType;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.ResidenceLevel;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.RecordResidenceReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.residence.QueryResidenceListReqDTO;
import com.xiaolian.amigo.data.network.model.residence.ResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QueryBriefSchoolListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolListReqDTO;
import com.xiaolian.amigo.data.network.model.user.BindResidenceReq;
import com.xiaolian.amigo.data.network.model.user.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.Residence;
import com.xiaolian.amigo.data.network.model.user.School;
import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.main.HomeFragment2;
import com.xiaolian.amigo.ui.user.adaptor.ListChooseAdaptor;
import com.xiaolian.amigo.ui.user.intf.IListChoosePresenter;
import com.xiaolian.amigo.ui.user.intf.IListChooseView;
import com.xiaolian.amigo.util.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.xiaolian.amigo.data.enumeration.Device.HEATER;


/**
 * 列表选择
 *
 * @author zcd
 * @date 17/9/15
 */

public class ListChoosePresenter<V extends IListChooseView> extends BasePresenter<V>
        implements IListChoosePresenter<V> {

    @SuppressWarnings("unused")
    private static final String TAG = ListChoosePresenter.class.getSimpleName();
    private IUserDataManager userDataManager;

    @Inject
    ListChoosePresenter(IUserDataManager userDataManager) {
        super();
        this.userDataManager = userDataManager;
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
                getMvpView().hideEmptyView();
                if (null == result.getError()) {
                    if (null != result.getData().getSchools() && result.getData().getSchools().size() > 0) {
                        ArrayList<ListChooseAdaptor.Item> schoolWapper = new ArrayList<>();
                        for (School school : result.getData().getSchools()) {
                            schoolWapper.add(new ListChooseAdaptor.Item(school,
                                    ObjectsCompat.equals(school.getId(), userDataManager.getUser().getSchoolId())));
                        }
                        getMvpView().addMore(schoolWapper);
                    } else {
                        getMvpView().showEmptyView();
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
        dto.setSchoolId(userDataManager.getUser().getSchoolId());
        // residencelevel 1 表示楼栋
        dto.setResidenceLevel(ResidenceLevel.BUILDING.getType());
        addObserver(userDataManager.queryResidenceList(dto), new NetworkObserver<ApiResult<ResidenceListRespDTO>>() {

            @Override
            public void onReady(ApiResult<ResidenceListRespDTO> result) {
                getMvpView().hideEmptyView();
                if (null == result.getError()) {
                    if (result.getData().getResidences() != null && result.getData().getResidences().size() > 0) {
                        ArrayList<ListChooseAdaptor.Item> wapper = new ArrayList<>();
                        for (Residence residence : result.getData().getResidences()) {
                            wapper.add(new ListChooseAdaptor.Item(residence));
                        }
                        getMvpView().addMore(wapper);
                    } else {
                        getMvpView().showEmptyView();
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
        addObserver(userDataManager.updateUserInfo(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess(R.string.change_school_success);
                    getMvpView().backToMain();
                    userDataManager.logout();
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
        addObserver(userDataManager.updateUserInfo(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {

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
        dto.setSchoolId(userDataManager.getUser().getSchoolId());
        // residencelevel 2 表示楼层
        dto.setResidenceLevel(ResidenceLevel.FLOOR.getType());
        addObserver(userDataManager.queryResidenceList(dto), new NetworkObserver<ApiResult<ResidenceListRespDTO>>() {

            @Override
            public void onReady(ApiResult<ResidenceListRespDTO> result) {
                getMvpView().hideEmptyView();
                if (null == result.getError()) {
                    if (result.getData().getResidences() != null && result.getData().getResidences().size() > 0) {
                        ArrayList<ListChooseAdaptor.Item> wapper = new ArrayList<>();
                        for (Residence residence : result.getData().getResidences()) {
                            wapper.add(new ListChooseAdaptor.Item(residence));
                        }
                        getMvpView().addMore(wapper);
                    } else {
                        getMvpView().showEmptyView();
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
        dto.setSchoolId(userDataManager.getUser().getSchoolId());
        // residencelevel 3 表示宿舍
        dto.setResidenceLevel(ResidenceLevel.ROOM.getType());
        addObserver(userDataManager.queryResidenceList(dto), new NetworkObserver<ApiResult<ResidenceListRespDTO>>() {

            @Override
            public void onReady(ApiResult<ResidenceListRespDTO> result) {
                getMvpView().hideEmptyView();
                if (null == result.getError()) {
                    if (result.getData().getResidences() != null && result.getData().getResidences().size() > 0) {
                        ArrayList<ListChooseAdaptor.Item> wapper = new ArrayList<>();
                        for (Residence residence : result.getData().getResidences()) {
                            wapper.add(new ListChooseAdaptor.Item(residence ));
                        }
                        getMvpView().addMore(wapper);
                    } else {
                        getMvpView().showEmptyView();
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
//        dto.setType(1);
        if (isEdit) {
            dto.setId(id);
        }
        addObserver(userDataManager.bindResidence(dto), new NetworkObserver<ApiResult<UserResidenceInListDTO>>() {

            @Override
            public void onReady(ApiResult<UserResidenceInListDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess(R.string.add_dormitory_success);
                    User user = userDataManager.getUser();
                    user.setResidenceName(result.getData().getResidenceName());
                    user.setDormitory(result.getData().getResidenceName());
                    user.setResidenceId(result.getData().getResidenceId());
                    userDataManager.setUser(user);
                    if (TextUtils.equals(activitySrc, Constant.EDIT_PROFILE_ACTIVITY_SRC)) {
                        getMvpView().backToEditDormitory();
                    } else if (TextUtils.equals(activitySrc, Constant.MAIN_ACTIVITY_SRC)) {
                        getMvpView().backToMain(activitySrc);
                    }else if (TextUtils.equals(activitySrc ,Constant.USER_INFO_ACTIVITY_SRC)) {
                        getMvpView().backToEditProfileActivity(result.getData().getResidenceName());
                    }
                    else {
                        getMvpView().backToDormitory();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void updateUser(long residenceId, String activitySrc) {
        PersonalUpdateReqDTO reqDTO = new PersonalUpdateReqDTO();
        reqDTO.setResidenceId(residenceId);
        addObserver(userDataManager.updateUserInfo(reqDTO) , new NetworkObserver<ApiResult<EntireUserDTO>>(){

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError() ){
                    User user = userDataManager.getUser();
                    EntireUserDTO entireUserDTO = result.getData();
                    user.setResidenceId(entireUserDTO.getResidenceId());
                    user.setResidenceName(entireUserDTO.getResidenceName());
                    user.setDormitory(entireUserDTO.getResidenceName());
                    userDataManager.setUser(user);
                    if (Constant.COMPLETE_INFO_ACTIVITY_SRC.equals(activitySrc)) /*跳转回完善资料页面*/{
                        getMvpView().backToCompeteInfoActivity(entireUserDTO.getResidenceName());
                    } else if (Constant.USER_INFO_ACTIVITY_SRC.equals(activitySrc)) /*跳转到编辑个人信息页面*/{
                        getMvpView().backToEditProfileActivity(entireUserDTO.getResidenceName());
                    }else if (Constant.USER_CERTIFICATION_STATUS_ACTIVITY_SRC.equals(activitySrc)){
                        getMvpView().backToUserCertification(entireUserDTO.getResidenceName());
                    }
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    @Override
    public void queryBathResidenceList(Integer page, Integer size, Integer deviceType) {
        QueryResidenceListReqDTO reqDTO = new QueryResidenceListReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(size);
        if (deviceType == Device.HEATER.getType()) {
            reqDTO.setBuildingType(BuildingType.DORMITORY.getType());
        }
        reqDTO.setDeviceType(deviceType);
        reqDTO.setSchoolId(userDataManager.getUser().getSchoolId());
        // residencelevel 1 表示楼栋
        reqDTO.setResidenceLevel(ResidenceLevel.BUILDING.getType());
        addObserver(userDataManager.queryBathResidenceList(reqDTO), new NetworkObserver<ApiResult<ResidenceListRespDTO>>() {

            @Override
            public void onReady(ApiResult<ResidenceListRespDTO> result) {
                getMvpView().hideEmptyView();
                if (null == result.getError()) {
                    if (result.getData().getResidences() != null && result.getData().getResidences().size() > 0) {
                        ArrayList<ListChooseAdaptor.Item> wapper = new ArrayList<>();
                        for (Residence residence : result.getData().getResidences()) {
                            wapper.add(new ListChooseAdaptor.Item(residence));
                        }
                        getMvpView().addMore(wapper);
                    } else {
                        getMvpView().showEmptyView();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getBathFloorList(Integer page, Integer size, Integer deviceType, Long parentId) {
        QueryResidenceListReqDTO dto = new QueryResidenceListReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        dto.setParentId(parentId);
        dto.setDeviceType(deviceType);
        dto.setSchoolId(userDataManager.getUser().getSchoolId());
        // residencelevel 2 表示楼层
        dto.setResidenceLevel(ResidenceLevel.FLOOR.getType());
        addObserver(userDataManager.queryBathResidenceList(dto), new NetworkObserver<ApiResult<ResidenceListRespDTO>>() {

            @Override
            public void onReady(ApiResult<ResidenceListRespDTO> result) {
                getMvpView().hideEmptyView();
                if (null == result.getError()) {
                    if (result.getData().getResidences() != null && result.getData().getResidences().size() > 0) {
                        ArrayList<ListChooseAdaptor.Item> wapper = new ArrayList<>();
                        for (Residence residence : result.getData().getResidences()) {
                            wapper.add(new ListChooseAdaptor.Item(residence));
                        }
                        getMvpView().addMore(wapper);
                    } else {
                        getMvpView().showEmptyView();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getBathroomList(Integer page, Integer size, Integer deviceType, Long parentId, boolean existDevice) {
        QueryResidenceListReqDTO dto = new QueryResidenceListReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        dto.setExistDevice(existDevice);
        dto.setDeviceType(deviceType);
        dto.setParentId(parentId);
        dto.setSchoolId(userDataManager.getUser().getSchoolId());
        // residencelevel 3 表示宿舍
        dto.setResidenceLevel(ResidenceLevel.ROOM.getType());
        addObserver(userDataManager.queryBathResidenceList(dto), new NetworkObserver<ApiResult<ResidenceListRespDTO>>() {

            @Override
            public void onReady(ApiResult<ResidenceListRespDTO> result) {
                getMvpView().hideEmptyView();
                if (null == result.getError()) {
                    if (result.getData().getResidences() != null && result.getData().getResidences().size() > 0) {
                        ArrayList<ListChooseAdaptor.Item> wapper = new ArrayList<>();
                        for (Residence residence : result.getData().getResidences()) {
                            wapper.add(new ListChooseAdaptor.Item(residence));
                        }
                        getMvpView().addMore(wapper);
                    } else {
                        getMvpView().showEmptyView();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void recordBath(long residenceId, int bathType , Long id ,String mac) {
        RecordResidenceReqDTO reqDTO = new RecordResidenceReqDTO();
        reqDTO.setResidenceId(residenceId);
        reqDTO.setBathType(bathType);
        reqDTO.setId(id);
        addObserver(userDataManager.recordBath(reqDTO) , new NetworkObserver<ApiResult<UserResidenceInListDTO>>(){
            @Override
            public void onReady(ApiResult<UserResidenceInListDTO> result) {
                if (null == result.getError()){
                    UserResidenceInListDTO dto = result.getData();
                    userDataManager.getUser().setRoomId(residenceId);
                    userDataManager.setRoomId(residenceId);
                    if (dto.isPubBath()){
                        getMvpView().startBathroom(dto);
                    }else{

                        //   请求接口 获取device信息
                        checkDeviceUsage();

                    }
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public boolean isStartBathroom(ListChooseAdaptor.Item residence) {
        if (!TextUtils.isEmpty(residence.getGroupId())){
            recordBath(residence.getId() ,2 ,null , residence.getMac());
            return false ;
        }
        return true ;
    }

    @Override
    public void checkDeviceUsage() {
        DeviceCheckReqDTO reqDTO = new DeviceCheckReqDTO();
        reqDTO.setDeviceType(HEATER.getType());
        addObserver(userDataManager.checkDeviceUseage(reqDTO), new NetworkObserver<ApiResult<DeviceCheckRespDTO>>() {

            @Override
            public void onReady(ApiResult<DeviceCheckRespDTO> result) {
                EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.ENABLE_VIEW));
                if (null == result.getError()) {
                    userDataManager.saveDeviceCategory(result.getData().getDevices());
                    getMvpView().startShower(result.getData().getLocation()
                            ,result.getData().getDefaultMacAddress() , result.getData().getDefaultSupplierId());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.ENABLE_VIEW));
            }
        });
    }

}
