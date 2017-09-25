package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryUserResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.data.network.model.user.UserResidence;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 编辑宿舍
 * <p>
 * Created by zcd on 9/19/17.
 */

public class EditDormitoryPresenter<V extends IEditDormitoryView> extends BasePresenter<V>
        implements IEditDormitoryPresenter<V> {
    private IUserDataManager manager;

    @Inject
    public EditDormitoryPresenter(IUserDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void queryDormitoryList(int page, int size) {
        SimpleQueryReqDTO dto = new SimpleQueryReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        addObserver(manager.queryUserResidenceList(dto), new NetworkObserver<ApiResult<QueryUserResidenceListRespDTO>>(){

            @Override
            public void onReady(ApiResult<QueryUserResidenceListRespDTO> result) {
                getMvpView().setRefreshing(false);
                getMvpView().setLoadMoreComplete();
                if (null == result.getError()) {
                    if (result.getData().getUserResidences() != null && result.getData().getUserResidences().size() > 0) {
                        List<EditDormitoryAdaptor.UserResidenceWrapper> wrappers = new ArrayList<>();
                        for (UserResidence userResidence : result.getData().getUserResidences()) {
                            wrappers.add(new EditDormitoryAdaptor.UserResidenceWrapper(userResidence,
                                    userResidence.getResidenceId() == manager.getUser().getResidenceId()));
                        }
                        getMvpView().addMore(wrappers);
                        getMvpView().addPage();
                    }
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void deleteDormitory(Long residenceId) {
        SimpleReqDTO dto = new SimpleReqDTO();
        dto.setId(residenceId);
        addObserver(manager.deleteResidence(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().showMessage("删除成功");
                    queryDormitoryList(1, Constant.PAGE_SIZE);
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void updateResidenceId(Long residenceId) {
        PersonalUpdateReqDTO dto = new PersonalUpdateReqDTO();
        dto.setResidenceId(residenceId);
        addObserver(manager.updateUserInfo(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    manager.setUser(new User(result.getData()));
                    getMvpView().notifyAdaptor();
                    getMvpView().showMessage("设置成功");
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
