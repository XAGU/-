package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.data.network.model.user.UserCertifyInfoRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

public interface IUserCertificationStatusView extends IBaseView {
    void setInfo(UserCertifyInfoRespDTO data);
}
