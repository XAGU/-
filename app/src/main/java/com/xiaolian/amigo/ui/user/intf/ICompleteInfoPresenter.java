package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.data.network.model.bathroom.BathRouteRespDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.data.vo.User;

public interface  ICompleteInfoPresenter < V extends  ICompleteInfoView>  extends IBasePresenter<V> {
    /*获取用户的初始个人信息*/
    User getUserInfo();

    /*更新性别*/
    void updateSex(int sex);

    void startShower(DeviceCheckRespDTO deviceCheckRespDTO);

}
