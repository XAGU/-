package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IUserCertificationPresenter <v extends IUserCertificationView> extends IBasePresenter<v> {


    User getUserInfo();
}
