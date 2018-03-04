package com.xiaolian.amigo.ui.credits.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 我的积分
 *
 * @author zcd
 * @date 18/2/23
 */

public interface ICreditsPresenter<V extends ICreditsView> extends IBasePresenter<V> {
    /**
     * 获取积分兑换规则
     */
    void getRules();

    /**
     * 兑换积分
     */
    void exchange(Long bonusId, Integer credits, Integer deviceType);

    /**
     * 获取缓存中的积分
     */
    void getCredits();

    /**
     * 积分兑换前校验
     */
    void checkForExchange(Long bonusId, Integer deviceType, String bonusAmount, Integer pointAmount);
}
