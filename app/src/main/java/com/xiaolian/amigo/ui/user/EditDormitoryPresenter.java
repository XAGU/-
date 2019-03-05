package com.xiaolian.amigo.ui.user;

import android.text.TextUtils;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.EmptyRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.user.DeleteResidenceRespDTO;
import com.xiaolian.amigo.data.network.model.user.QueryUserResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.user.ResidenceUpdateRespDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryView;
import com.xiaolian.amigo.util.RxHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * 编辑宿舍
 *
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

    @Override


    public void saveDefaultResidenceId(Long residenceId) {
        userDataManager.getUser().setResidenceId(residenceId);
    }

    @Override
    public void queryBathList() {
        EmptyRespDTO emptyRespDTO = new EmptyRespDTO();
        addObserver(userDataManager.bathList(emptyRespDTO), new NetworkObserver<ApiResult<QueryUserResidenceListRespDTO>>() {

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
     *
     * @param dto
     * @return
     */
    private List<EditDormitoryAdaptor.UserResidenceWrapper> addBathroomListItem(QueryUserResidenceListRespDTO dto) {
        List<EditDormitoryAdaptor.UserResidenceWrapper> wrappers = new ArrayList<>();

        for (UserResidenceInListDTO userResidence : dto.getUserResidences()) {
            wrappers.add(new EditDormitoryAdaptor.UserResidenceWrapper(userResidence));
        }
        return wrappers;
    }

    @Override
    public void deleteBathroomRecord(long id, int position, boolean isDefault) {
        if (isDefault) {
            getMvpView().onError("当前浴室为默认浴室，无法删除");
        } else {
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
    public void updateNormalBathroom(EditDormitoryAdaptor.UserResidenceWrapper wrapper, int currentPosition) {
        SimpleReqDTO dto = new SimpleReqDTO();
        dto.setId(wrapper.getId());
        addObserver(userDataManager.updateNormalBathroom(dto), new NetworkObserver<ApiResult<ResidenceUpdateRespDTO>>() {

            @Override
            public void onReady(ApiResult<ResidenceUpdateRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getResult()) {
                        if (null != result.getData().getTimeValid() && result.getData().getTimeValid()) {
                            getMvpView().notifyAdapter(wrapper, currentPosition);
                            RxHelper.delay(300, TimeUnit.MILLISECONDS)
                                    .subscribe(integer -> {
                                        if (wrapper.isPubBath()) {
                                            getMvpView().startBathroom(wrapper.getResidence());
                                        } else {
                                            if (TextUtils.isEmpty(wrapper.getMacAddress())) {
                                                getMvpView().onError("该地址下无设备");
                                            } else {
                                                getMvpView().startShower(wrapper.getResidence());
                                            }
                                        }
                                    });
                        } else {
                            getMvpView().showTimeValidDialog(result.getData().getTitle(), result.getData().getRemark(),
                                    wrapper, currentPosition);
                        }
                    } else {
                        getMvpView().onError(result.getData().getFailReason());
                    }

                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());

                }
            }
        });
    }


}
