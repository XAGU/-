package com.xiaolian.amigo.ui.bonus.intf;

import com.xiaolian.amigo.di.BonusActivityContext;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 代金券Presenter接口
 *
 * @author zcd
 * @date 17/9/18
 */
@BonusActivityContext
public interface IBonusPresenter<V extends IBonusView> extends IBasePresenter<V> {

    /**
     * 请求红包列表
     *
     * @param page       页数
     * @param deviceType 设备类型
     * @param checkUse   是否使用
     */
    void requestBonusList(int page, Integer deviceType, boolean checkUse);

    /**
     * 请求已过期红包
     *
     * @param page 页数
     */
    void requestExpiredBonusList(int page);
}
