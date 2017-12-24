package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.data.vo.LostAndFound;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 失物招领详情
 * <p>
 * Created by zcd on 9/21/17.
 */

public interface ILostAndFoundDetailView extends IBaseView {
    void render(LostAndFound lostAndFound);
}
