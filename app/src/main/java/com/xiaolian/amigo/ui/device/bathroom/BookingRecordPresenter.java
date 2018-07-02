package com.xiaolian.amigo.ui.device.bathroom;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingRecordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingRecordView;

import javax.inject.Inject;

/**
 * 预约记录
 * @author zcd
 * @date 18/6/29
 */
public class BookingRecordPresenter<V extends IBookingRecordView> extends BasePresenter<V>
        implements IBookingRecordPresenter<V> {
    private IBathroomDataManager bathroomDataManager;

    @Inject
    public BookingRecordPresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }
}
