package com.xiaolian.amigo.ui.device.bathroom;

import com.xiaolian.amigo.data.enumeration.BathTradeType;
import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingRecordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingRecordView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 预约记录
 * @author zcd
 * @date 18/6/29
 */
public class BookingRecordPresenter<V extends IBookingRecordView> extends BasePresenter<V>
        implements IBookingRecordPresenter<V> {
    private IBathroomDataManager bathroomDataManager;

    private int page = Constant.PAGE_START_NUM ;
    private final  int pageSize = Constant.PAGE_SIZE ;
    private List<QueryBathOrderListRespDTO.OrdersBean> ordersBeanList ;

    @Inject
    public BookingRecordPresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }

    @Override
    public void getBookingRecordList() {
        QueryBathOrderListReqDTO reqDTO = new QueryBathOrderListReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(pageSize);
        reqDTO.setType(BathTradeType.BOOKING.getCode());
        reqDTO.setUserId(bathroomDataManager.getUserId());
        addObserver(bathroomDataManager.getOrderRecordList(reqDTO) , new NetworkObserver<ApiResult<QueryBathOrderListRespDTO>>(){

            @Override
            public void onReady(ApiResult<QueryBathOrderListRespDTO> result) {
                ordersBeanList = new ArrayList<>();
                ordersBeanList.clear();
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()){
                    if (result.getData().getOrders() != null && result.getData().getOrders().size() > 0 ){
                        ordersBeanList.addAll(result.getData().getOrders());
                    }else{
                        if (ordersBeanList.isEmpty() && page == Constant.PAGE_START_NUM){
                            getMvpView().showEmptyView();
                            getMvpView().setSuccessfulAppointments(result.getData().getSuccessTimes());
                            getMvpView().setMissedAppointments(result.getData().getMissedTimes());
                            return ;
                        }
                    }
                    getMvpView().addMore(ordersBeanList);
                    page++ ;
                    getMvpView().hideEmptyView();
                    getMvpView().setSuccessfulAppointments(result.getData().getSuccessTimes());
                    getMvpView().setMissedAppointments(result.getData().getMissedTimes());
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
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
