package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IChooseSchoolPresenter<v extends IChooseSchoolView> extends IBasePresenter<v> {

    void getSchoolNameList();


    void updataSchool(Long id);
}
