package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.data.network.model.bathroom.BathPasswordUpdateReqDTO;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

import java.util.List;

public interface IFindBathroomPasswordPresenter<V extends IFindBathroomPasswordView>  extends IBasePresenter<V>{
    void updateBathroomPassword(String password );
    String getMobile();
    void getVerifyCode(String mobile);

    void checkVerifyCode(String mobile, String code);

    List<String> getBathroomPasswordDesc();

}
