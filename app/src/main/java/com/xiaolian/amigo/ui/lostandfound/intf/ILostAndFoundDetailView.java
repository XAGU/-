package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.data.vo.LostAndFound;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 失物招领详情
 *
 * @author zcd
 * @date 17/9/21
 */

public interface ILostAndFoundDetailView extends IBaseView {
    /**
     * 显示失物找零
     *
     * @param lostAndFound 失物招领
     */
    void render(LostAndFound lostAndFound);

}
