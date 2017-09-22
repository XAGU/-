package com.xiaolian.amigo.ui.notice.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.notice.adaptor.NoticeAdaptor;

import java.util.List;

/**
 * 通知中心
 * <p>
 * Created by zcd on 9/22/17.
 */

public interface INoticeView extends IBaseView {
    void addMore(List<NoticeAdaptor.NoticeWapper> wapper);
}
