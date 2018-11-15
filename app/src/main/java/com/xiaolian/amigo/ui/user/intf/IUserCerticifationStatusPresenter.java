package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IUserCerticifationStatusPresenter <v extends IUserCertificationStatusView> extends IBasePresenter<v> {


    void getCertifyInfo();

    void getDormitory();


    String getDormInfo();

    void setCertifyStatus(int certificationReviewing);
}
