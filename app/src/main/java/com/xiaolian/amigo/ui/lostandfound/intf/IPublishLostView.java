package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 失物发布
 *
 * @author zcd
 * @date 17/9/21
 */

public interface IPublishLostView extends IBaseView {
    /**
     * 结束页面
     */
    void finishView();

    /**
     * 变更提交按钮的状态
     */
    void toggleBtnStatus();

    /**
     * 添加图片
     *
     * @param url      url
     * @param position 位置
     */
    void addImage(String url, int position);


}
