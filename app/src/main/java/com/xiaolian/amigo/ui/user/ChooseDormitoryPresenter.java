package com.xiaolian.amigo.ui.user;

import android.support.v4.util.ObjectsCompat;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.user.QueryUserResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;
import com.xiaolian.amigo.ui.user.intf.IChooseDormitoryPresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseDormitoryView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 宿舍列表
 *
 * @author zcd
 * @date 17/10/11
 */
@Deprecated
public class ChooseDormitoryPresenter<V extends IChooseDormitoryView> extends BasePresenter<V>
        implements IChooseDormitoryPresenter<V> {

    private IUserDataManager userDataManager;

    @Inject
    ChooseDormitoryPresenter(IUserDataManager userDataManager) {
        super();
        this.userDataManager = userDataManager;
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
                        getMvpView().showEmptyView();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().showErrorView();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().showErrorView();
            }
        });
    }
}
