package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 提现详情
 *
 * @author zcd
 * @date 17/10/23
 */

public interface IWithdrawalDetailPresenter<V extends IWithdrawalDetailView> extends IBasePresenter<V> {
    /**
     * 请求数据
     *
     * @param id 提现账单id
     */
    void requestData(Long id);

    /**
     * 获取token
     *
     * @return token
     */
    String getToken();

    /**
     * 提醒客服
     *
     * @param sourceId 账单id
     */
    void remind(Long sourceId);

    /**
     * 取消提现
     *
     * @param id 账单id
     */
    void cancelWithdraw(Long id);

    /**
     * 投诉
     *
     * @param id   账单id
     * @param type 投诉类型
     */
    void complaint(Long id, int type);
}
