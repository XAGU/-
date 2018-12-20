package com.xiaolian.amigo.ui.more.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 更多页面
 *
 * @author zcd
 * @date 17/10/13
 */

public interface IMoreView extends IBaseView {
    /**
     * 返回主页
     */
    void backToMain();

    /**
     * 是否显示校OK迁移入口
     */
    void showTransfer();


}
