package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 充值详情
 *
 * @author zcd
 * @date 17/10/23
 */

public interface IRechargeDetailPresenter<V extends IRechargeDetailView> extends IBasePresenter<V> {
    /**
     * 获取数据
     *
     * @param id 充值订单id
     */
    void requestData(Long id);

    /**
     * 提醒
     *
     * @param id id
     */
    void remind(Long id);

    /**
     * 投诉
     *
     * @param id   账单id
     * @param type 投诉类型
     */
    void complaint(Long id, int type);

    /**
     * 获取手机号
     *
     * @return 手机号
     */
    String getMobile();

    String getAccessToken();

    String getRefreshToken();
}
