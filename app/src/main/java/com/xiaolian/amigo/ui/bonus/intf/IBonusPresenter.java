package com.xiaolian.amigo.ui.bonus.intf;

import com.xiaolian.amigo.di.BonusActivityContext;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 代金券Presenter接口
 * @author zcd
 */
@BonusActivityContext
public interface IBonusPresenter<V extends IBonusView> extends IBasePresenter<V> {

    void requestBonusList(int page, Integer deviceType, boolean checkUse);

    void requestExpiredBonusList(int page);
}
