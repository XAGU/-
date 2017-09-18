package com.xiaolian.amigo.ui.bonus.intf;

import com.xiaolian.amigo.di.BonusActivityContext;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 红包Presenter接口
 * @author zcd
 */
@BonusActivityContext
public interface IBonusPresenter<V extends IBonusView> extends IBasePresenter<V> {
}
