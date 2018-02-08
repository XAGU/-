package com.xiaolian.amigo.ui.user;

import android.support.v4.util.ObjectsCompat;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
    public void queryDormitoryList(int page, int size) {
        SimpleQueryReqDTO dto = new SimpleQueryReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        addObserver(userDataManager.queryUserResidenceList(dto), new NetworkObserver<ApiResult<QueryUserResidenceListRespDTO>>(false, true) {

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
                            wrappers.add(new EditDormitoryAdaptor.UserResidenceWrapper(userResidence.transform(),
                                    ObjectsCompat.equals(userResidence.getResidenceId(),
                                            userDataManager.getUser().getResidenceId())));
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
}
