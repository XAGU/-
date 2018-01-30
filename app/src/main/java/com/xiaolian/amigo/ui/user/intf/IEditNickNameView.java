package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 编辑昵称View接口
 *
 * @author zcd
 * @date 17/9/15
 */
public interface IEditNickNameView extends IBaseView {
    /**
     * 清除编辑框
     */
    void clearEditText();

    /**
     * 结束页面
     */
    void finishView();
}
