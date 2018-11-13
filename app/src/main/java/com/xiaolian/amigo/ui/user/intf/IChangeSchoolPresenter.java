package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IChangeSchoolPresenter<v extends IChangeSchoolView> extends IBasePresenter<v> {

    void cancelApplySchool(long id);



    void realChangeSchool(long id,String reason);

    void preChangeSchool();

    void requestForRelogin();

}
