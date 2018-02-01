package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 失物招领详情
 *
 * @author zcd
 * @date 17/9/21
 */

public interface ILostAndFoundDetailPresenter<V extends ILostAndFoundDetailView> extends IBasePresenter<V> {
    /**
     * 获取失物招领详情
     *
     * @param id id
     */
    void getLostAndFoundDetail(Long id);
}
