package com.xiaolian.amigo.ui.notice.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 通知中心
 * <p>
 * Created by zcd on 9/22/17.
 */

public interface INoticePresenter<V extends INoticeView> extends IBasePresenter<V> {
    void requestNotices(Integer page);

    void readUrgentNotify(Long id);
}
