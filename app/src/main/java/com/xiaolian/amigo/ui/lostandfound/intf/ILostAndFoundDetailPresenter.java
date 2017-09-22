package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 失物招领详情
 * <p>
 * Created by zcd on 9/21/17.
 */

public interface ILostAndFoundDetailPresenter<V extends ILostAndFoundDetailView> extends IBasePresenter<V> {
    void getLostAndFoundDetail(Long id);
}
