package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * @author zcd
 * @date 18/6/29
 */
public interface IBookingView extends IBaseView {
    void bookingSuccess(BathOrderRespDTO data);

    void bookingCancel();
}
