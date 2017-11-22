package com.xiaolian.amigo.ui.notice.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.notice.adaptor.NoticeAdaptor;

import java.util.List;

/**
 * 通知中心
 * <p>
 * Created by zcd on 9/22/17.
 */

public interface INoticeView extends IBaseListView {
    void addMore(List<NoticeAdaptor.NoticeWapper> wapper);

    void readNotify(Long id);
}
