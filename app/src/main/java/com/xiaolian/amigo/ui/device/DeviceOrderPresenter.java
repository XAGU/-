package com.xiaolian.amigo.ui.device;

import com.xiaolian.amigo.data.manager.intf.IBonusDataManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bonus.Bonus;
import com.xiaolian.amigo.data.network.model.dto.request.OrderDetailReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryUserBonusReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderDetailRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryUserBonusListRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.ui.bonus.intf.IBonusPresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusView;
import com.xiaolian.amigo.ui.device.intf.IDeviceOrderPresenter;
import com.xiaolian.amigo.ui.device.intf.IDeviceOrderView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DeviceOrderPresenter<V extends IDeviceOrderView> extends BasePresenter<V>
        implements IDeviceOrderPresenter<V> {

    private static final String TAG = DeviceOrderPresenter.class.getSimpleName();
    private IOrderDataManager manager;

    @Inject
    public DeviceOrderPresenter(IOrderDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void onLoad(long orderId) {
        OrderDetailReqDTO reqDTO = new OrderDetailReqDTO();
        reqDTO.setId(orderId);

        addObserver(manager.queryOrderDetail(reqDTO), new NetworkObserver<ApiResult<OrderDetailRespDTO>>() {
            @Override
            public void onReady(ApiResult<OrderDetailRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().setRefreshComplete(result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
