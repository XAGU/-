package com.xiaolian.amigo.ui.device.bathroom.intf;

/**
 * @author zcd
 * @date 18/6/29
 */

import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

import java.util.List;

public interface IBookingRecordView extends IBaseView {

    void setRefreshComplete();

    void setLoadMoreComplete();

    void showErrorView();

    void hideEmptyView();

    void hideErrorView();

    void showEmptyView();

    void addMore(List<QueryBathOrderListRespDTO.OrdersBean> ordersBeanList);

    void onError(String message);

    /**
     * 设置本月预约成功次数
     * @param num
     */
    void setSuccessfulAppointments(String num);

    /**
     * 设置本月失约次数
     */
    void setMissedAppointments(String num);

}
