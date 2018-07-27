package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * @author zcd
 * @date 18/6/29
 */
public interface IBuyCodePresenter<V extends IBuyCodeView> extends IBasePresenter<V> {

    /**
     * 购买洗澡卷前操作
     */
    void preBuyVoucher();

    /**
     * 购买洗澡卷
     */
    void pay(Double prepayAmount ,Long bonusId);

    /**
     * 取消购买洗澡卷
     */
    void cancel(Long bathOrderId);

    /**
     * 倒计时
     * @param time
     */
    void BathroomCountDown(int time);



}
