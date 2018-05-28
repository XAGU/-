package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * @author zcd
 * @date 18/5/14
 */
public interface IPublishLostAndFoundView extends IBaseView {

    void finishView();

    void addImage(String url, int position);
}
