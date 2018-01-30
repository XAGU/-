package com.xiaolian.amigo.ui.more.intf;

import com.xiaolian.amigo.data.network.model.version.VersionDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 关于我们
 *
 * @author zcd
 * @date 17/11/9
 */

public interface IAboutUsView extends IBaseView {
    /**
     * 显示更新对话框
     *
     * @param version 版本信息
     */
    void showUpdateDialog(VersionDTO version);

    /**
     * 显示更新按钮
     *
     * @param version 版本信息
     */
    void showUpdateButton(VersionDTO version);
}
