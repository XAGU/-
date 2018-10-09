package com.xiaolian.amigo.ui.notice.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * @author zcd
 * @date 18/7/20
 */
public interface INoticeDetailPresenter<V extends INoticeDetailView> extends IBasePresenter<V> {
    void getNoticeDetail(Long id);
}
