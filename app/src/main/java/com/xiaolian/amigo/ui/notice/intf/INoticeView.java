package com.xiaolian.amigo.ui.notice.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.notice.adaptor.NoticeAdaptor;

import java.util.List;

/**
 * 通知中心
 *
 * @author zcd
 * @date 17/9/22
 */

public interface INoticeView extends IBaseListView {
    /**
     * 加载通知列表
     *
     * @param wapper 通知列表
     */
    void addMore(List<NoticeAdaptor.NoticeWapper> wapper);

    /**
     * 已读通知
     *
     * @param id 通知id
     */
    void readNotify(Long id);
}
