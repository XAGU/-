package com.xiaolian.amigo.ui.user;

import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;
import android.util.Log;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.EmptyRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.user.DeleteResidenceRespDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.QueryUserResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryView;
import com.xiaolian.amigo.util.RxHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.internal.util.ObserverSubscriber;

/**
 * 编辑宿舍
 * @author zcd
 * @date 17/9/19
 */

public class EditDormitoryPresenter<V extends IEditDormitoryView> extends BasePresenter<V>
        implements IEditDormitoryPresenter<V> {
    private IUserDataManager userDataManager;

    @Inject
    EditDormitoryPresenter(IUserDataManager manager) {
        super();
        this.userDataManager = manager;
    }
    @Deprecated
    @Override
    public void queryDormitoryList(int page, int size) {
//        SimpleQueryReqDTO dto = new SimpleQueryReqDTO();
//        dto.setPage(page);
//        dto.setSize(size);
        addObserver(userDataManager.queryUserResidenceList(), new NetworkObserver<ApiResult<QueryUserResidenceListRespDTO>>(false, true) {



            @Override
            public void onReady(ApiResult<QueryUserResidenceListRespDTO> result) {
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();

                if (null == result.getError()) {
                    if (result.getData().getUserResidences() != null && result.getData().getUserResidences().size() > 0) {
                        List<EditDormitoryAdaptor.UserResidenceWrapper> wrappers = new ArrayList<>();
                        for (UserResidenceInListDTO userResidence : result.getData().getUserResidences()) {
//                            wrappers.add(new EditDormitoryAdaptor.UserResidenceWrapper(userResidence.transform(),
//                                    ObjectsCompat.equals(userResidence.getResidenceId(),
//                                            userDataManager.getUser().getResidenceId())));
                        }
                        getMvpView().addMore(wrappers);
                        getMvpView().addPage();
                    } else {
                        getMvpView().addMore(new ArrayList<>());
                        getMvpView().showEmptyView();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().addMore(new ArrayList<>());
                    getMvpView().showErrorView();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().showErrorView();
                getMvpView().addMore(new ArrayList<>());
            }
        });
    }

    @Deprecated
    @Override
    public void deleteDormitory(Long residenceId) {
        SimpleReqDTO dto = new SimpleReqDTO();
        dto.setId(residenceId);
        addObserver(userDataManager.deleteResidence(dto), new NetworkObserver<ApiResult<DeleteResidenceRespDTO>>() {

            @Override
            public void onReady(ApiResult<DeleteResidenceRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess(R.string.delete_success);
                    getMvpView().refreshList(result.getData().getResidenceId());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Deprecated
    @Override
    public void updateResidenceId(Long residenceId) {
        PersonalUpdateReqDTO dto = new PersonalUpdateReqDTO();
        dto.setResidenceId(residenceId);
        addObserver(userDataManager.updateUserInfo(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    userDataManager.setUser(new User(result.getData()));
                    getMvpView().notifyAdaptor();
                    getMvpView().onSuccess(R.string.setting_success);
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override


    public void saveDefaultResidenceId(Long residenceId) {
        userDataManager.getUser().setResidenceId(residenceId);
    }
    @Deprecated
    @Override
    public void queryDormitoryDetail(Long id, int position) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(id);
        addObserver(userDataManager.queryResidenceDetail(reqDTO),
                new NetworkObserver<ApiResult<UserResidenceDTO>>() {

                    @Override
                    public void onReady(ApiResult<UserResidenceDTO> result) {
                        if (null == result.getError()) {
                            getMvpView().editDormitory(id, result.getData(), position);
                        } else {
                            getMvpView().editDormitory(id, null, position);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().editDormitory(id, null, position);
                    }
                });
    }

    @Override
    public void queryBathList() {
        EmptyRespDTO emptyRespDTO = new EmptyRespDTO();
        addObserver(userDataManager.bathList(emptyRespDTO),new NetworkObserver<ApiResult<QueryUserResidenceListRespDTO>>(){

            @Override
            public void onStart() {

            }
            @Override
            public void onReady(ApiResult<QueryUserResidenceListRespDTO> result) {
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (result.getData().getUserResidences() != null && result.getData().getUserResidences().size() > 0) {
                        List<EditDormitoryAdaptor.UserResidenceWrapper> wrappers = addBathroomListItem(result.getData());
                        getMvpView().addMore(wrappers);
                    } else {
                        getMvpView().addMore(new ArrayList<>());
                        getMvpView().showEmptyView();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().addMore(new ArrayList<>());
                    getMvpView().showErrorView();
                }
            }



        @Override
        public void onError(Throwable e) {
            super.onError(e);
            getMvpView().setRefreshComplete();
            getMvpView().setLoadMoreComplete();
            getMvpView().showErrorView();
            getMvpView().addMore(new ArrayList<>());
        }
    });
    }


    /**
     * 添加数据
     * @param dto
     * @return
     */
    private List<EditDormitoryAdaptor.UserResidenceWrapper> addBathroomListItem(QueryUserResidenceListRespDTO dto){
        List<EditDormitoryAdaptor.UserResidenceWrapper> wrappers = new ArrayList<>();

        for (UserResidenceInListDTO userResidence : dto.getUserResidences()) {
            EditDormitoryAdaptor.UserResidenceWrapper userResidenceWrapper = null ;
            if (ObjectsCompat.equals(userResidence.getResidenceId() , userDataManager.getRoomId())){
                userResidenceWrapper = new EditDormitoryAdaptor.UserResidenceWrapper(
                        userResidence ,true );
                wrappers.add(userResidenceWrapper);
                int position = wrappers.indexOf(userResidenceWrapper);
                getMvpView().setLastNormalPosition(position);
            }else{
                userResidenceWrapper = new EditDormitoryAdaptor.UserResidenceWrapper(
                        userResidence ,false );
                wrappers.add(userResidenceWrapper);
            }
        }
        return wrappers ;
    }

    @Override
    public void deleteBathroomRecord(long id , int position , boolean isDefault) {
        if (isDefault){
            getMvpView().onError("当前浴室为默认浴室，无法删除");
        }else {
            SimpleReqDTO simpleReqDTO = new SimpleReqDTO();
            simpleReqDTO.setId(id);
            addObserver(userDataManager.deleteBathRecord(simpleReqDTO), new NetworkObserver<ApiResult<DeleteResidenceRespDTO>>() {
                @Override
                public void onReady(ApiResult<DeleteResidenceRespDTO> result) {
                    if (null == result.getError()) {
                        getMvpView().notifyAdaptor();
                    } else {
                        getMvpView().onError(result.getError().getDisplayMessage());
                    }
                }
            });
        }
    }

    @Override
    public void  updateNormalBathroom(EditDormitoryAdaptor.UserResidenceWrapper wrapper  , int  currentPosition) {
        SimpleReqDTO dto = new SimpleReqDTO();
        dto.setId(wrapper.getId());
        addObserver(userDataManager.updateNormalBathroom(dto) , new NetworkObserver<ApiResult<BooleanRespDTO>>(){

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()){
                    if (result.getData().isResult()) {
                        saveNormalBathroomId(wrapper.getResidenceId());
                        userDataManager.setRoomId(wrapper.getResidenceId());
                        getMvpView().notifyAdapter( wrapper,currentPosition);
                        RxHelper.delay(300 ,TimeUnit.MILLISECONDS)
                                .subscribe(new Action1<Integer>() {
                                    @Override
                                    public void call(Integer integer) {
                                        if (wrapper.isPubBath()){
                                            getMvpView().startBathroom(wrapper.getResidence());
                                        }else{
                                            if (TextUtils.isEmpty(wrapper.getMacAddress())){
                                                getMvpView().onError("该地址下无设备");
                                            }else {
                                                getMvpView().startShower(wrapper.getResidence());
                                            }
                                        }
                                    }
                                });
                    }else{
                        getMvpView().onError(result.getData().getFailReason());
                    }

                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());

                }
            }
        });
    }

    /**
     * 存储默认的洗澡地址
     * @param id
     */
    private void saveNormalBathroomId(Long id){

        User user = userDataManager.getUser() ;
        user.setRoomId(id);
        userDataManager.setUser(user);
    }

}
