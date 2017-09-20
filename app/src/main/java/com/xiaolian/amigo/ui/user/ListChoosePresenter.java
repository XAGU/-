package com.xiaolian.amigo.ui.user;

import android.util.Log;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.BindResidenceReq;
import com.xiaolian.amigo.data.network.model.dto.request.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryResidenceListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryBriefSchoolListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.UserResidenceInListDTO;
import com.xiaolian.amigo.data.network.model.user.Residence;
import com.xiaolian.amigo.data.network.model.user.School;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.adaptor.ListChooseAdaptor;
import com.xiaolian.amigo.ui.user.intf.IListChoosePresenter;
import com.xiaolian.amigo.ui.user.intf.IListChooseView;

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
        this.manager = manager;
    }


    @Override
    public void getSchoolList(Integer page, Integer size) {
        SimpleQueryReqDTO dto = new SimpleQueryReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        addObserver(manager.getSchoolList(dto), new NetworkObserver<ApiResult<QueryBriefSchoolListRespDTO>>(){

            @Override
            public void onReady(ApiResult<QueryBriefSchoolListRespDTO> result) {
                if (null == result.getError()) {
                    if (null != result.getData().getSchools() && result.getData().getSchools().size() > 0) {
                        ArrayList<ListChooseAdaptor.Item> schoolWapper = new ArrayList<>();
                        for (School school : result.getData().getSchools()) {
                            schoolWapper.add(new ListChooseAdaptor.Item(school,
                                    school.getId() == manager.getUser().getSchoolId()));
                        }
                        getMvpView().addMore(schoolWapper);
                    }
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getBuildList(Integer page, Integer size) {
        QueryResidenceListReqDTO dto = new QueryResidenceListReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        // buildtype 1 表示宿舍
        dto.setBuildingType(1);
        dto.setSchoolId(manager.getUser().getSchoolId());
        // residencelevel 1 表示楼栋
        dto.setResidenceLevel(1);
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
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void updateSchool(Integer schoolId) {
        PersonalUpdateReqDTO dto = new PersonalUpdateReqDTO();
        dto.setSchoolId(schoolId);
        addObserver(manager.updateUserInfo(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    getMvpView().showMessage("修改成功");
                    getMvpView().finishView();
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
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
                    getMvpView().showMessage("修改成功");
                    getMvpView().finishView();
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });

    }

    @Override
    public void getFloorList(int page, int size, int parentId) {
        QueryResidenceListReqDTO dto = new QueryResidenceListReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        // buildtype 1 表示宿舍
        dto.setBuildingType(1);
        dto.setParentId(parentId);
        dto.setSchoolId(manager.getUser().getSchoolId());
        // residencelevel 2 表示楼层
        dto.setResidenceLevel(2);
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
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getDormitoryList(int page, int size, int parentId) {
        QueryResidenceListReqDTO dto = new QueryResidenceListReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        // buildtype 1 表示宿舍
        dto.setBuildingType(1);
        dto.setParentId(parentId);
        dto.setSchoolId(manager.getUser().getSchoolId());
        // residencelevel 3 表示宿舍
        dto.setResidenceLevel(3);
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
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void bindDormitory(int id, int residenceId, boolean isEdit) {
        BindResidenceReq dto = new BindResidenceReq();
        dto.setResidenceId(residenceId);
        if (isEdit) {
            dto.setId(id);
        }
        addObserver(manager.bindResidence(dto), new NetworkObserver<ApiResult<UserResidenceInListDTO>>() {

            @Override
            public void onReady(ApiResult<UserResidenceInListDTO> result) {
                if (null == result.getError()) {
                    getMvpView().showMessage("绑定成功");
                    getMvpView().backToDormitory();
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
