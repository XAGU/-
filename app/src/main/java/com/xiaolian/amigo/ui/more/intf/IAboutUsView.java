package com.xiaolian.amigo.ui.more.intf;

import com.xiaolian.amigo.data.network.model.dto.response.VersionDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 关于我们
 * <p>
 * Created by zcd on 17/11/9.
 */

public interface IAboutUsView extends IBaseView {
    void showUpdateDialog(VersionDTO version);

    void showUpdateButton(VersionDTO version);
}
