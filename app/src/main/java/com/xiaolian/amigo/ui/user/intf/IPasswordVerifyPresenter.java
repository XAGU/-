package com.xiaolian.amigo.ui.user.intf;

import android.widget.Button;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IPasswordVerifyPresenter <V extends IPasswordVerifyView> extends IBasePresenter<V> {
    void verifyPassword(String passsword , Button button);
}
