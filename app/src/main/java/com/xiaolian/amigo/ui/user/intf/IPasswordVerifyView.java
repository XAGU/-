package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

public interface IPasswordVerifyView extends IBaseView {
    void showTipDialog(String title,String tip);
    void goToChangeView();
}
