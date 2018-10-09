package com.xiaolian.amigo.ui.notice.intf;

import com.xiaolian.amigo.data.network.model.notify.NotifyDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * @author zcd
 * @date 18/7/20
 */
public interface INoticeDetailView extends IBaseView {
    void render(NotifyDTO data);
}
