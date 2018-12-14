package com.xiaolian.amigo.ui.user.intf;

import android.widget.Button;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IChangePhonePresenter <v extends IChangePhoneView> extends IBasePresenter<v> {

    void getVerification(String mobile , Button button);

    void changePhoneNumber(String mobile, String code , Button button);

}
