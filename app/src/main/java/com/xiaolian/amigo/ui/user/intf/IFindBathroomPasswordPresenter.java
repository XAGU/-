package com.xiaolian.amigo.ui.user.intf;

import android.widget.Button;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

import java.util.List;

public interface IFindBathroomPasswordPresenter<V extends IFindBathroomPasswordView>  extends IBasePresenter<V>{

    void updateBathroomPassword(String password  , Button button);

    String getMobile();

    void getVerifyCode(String mobile , Button button);

    void checkVerifyCode(String mobile, String code , Button button);

    List<String> getBathroomPasswordDesc();

}
