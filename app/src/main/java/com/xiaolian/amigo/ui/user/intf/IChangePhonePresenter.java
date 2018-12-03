package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IChangePhonePresenter <v extends IChangePhoneView> extends IBasePresenter<v> {
    void getVerification(String mobile);
    void changePhoneNumber(String mobile, String code);

}
