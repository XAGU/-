package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryDeviceListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryDeviceListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryUserResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidence;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.heater.HeaterActivity;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;
import com.xiaolian.amigo.ui.user.intf.IChooseDormitoryPresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseDormitoryView;
import com.xiaolian.amigo.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 宿舍列表
 * <p>
 * Created by zcd on 10/11/17.
 */

public class ChooseDormitoryPresenter<V extends IChooseDormitoryView> extends BasePresenter<V>
    implements IChooseDormitoryPresenter<V> {

    private IUserDataManager manager;

    @Inject
    public ChooseDormitoryPresenter(IUserDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void queryDormitoryList(int page, int size) {
        SimpleQueryReqDTO dto = new SimpleQueryReqDTO();
        dto.setPage(page);
        dto.setSize(size);
        addObserver(manager.queryUserResidenceList(dto), new NetworkObserver<ApiResult<QueryUserResidenceListRespDTO>>(false, true){

            @Override
            public void onReady(ApiResult<QueryUserResidenceListRespDTO> result) {
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (result.getData().getUserResidences() != null && result.getData().getUserResidences().size() > 0) {
                        List<EditDormitoryAdaptor.UserResidenceWrapper> wrappers = new ArrayList<>();
                        for (UserResidence userResidence : result.getData().getUserResidences()) {
                            wrappers.add(new EditDormitoryAdaptor.UserResidenceWrapper(userResidence,
                                    CommonUtil.equals(userResidence.getResidenceId(),
                                            manager.getUser().getResidenceId())));
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
