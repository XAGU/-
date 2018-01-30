package com.xiaolian.amigo.ui.notice.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 通知中心
 *
 * @author zcd
 * @date 17/9/22
 */

public interface INoticePresenter<V extends INoticeView> extends IBasePresenter<V> {
    /**
     * 获取通知
     *
     * @param page 页数
     */
    void requestNotices(Integer page);

    /**
     * 将通知设置为已读
     *
     * @param id 通知id
     */
    void readUrgentNotify(Long id);
}
