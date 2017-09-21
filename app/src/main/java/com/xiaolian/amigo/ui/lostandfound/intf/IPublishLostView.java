package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 失物发布
 * <p>
 * Created by zcd on 9/21/17.
 */

public interface IPublishLostView extends IBaseView {
    void finishView();

    // 变更提交按钮的状态
    void toggleBtnStatus();

    // 添加图片
    void addImage(String url);


}
