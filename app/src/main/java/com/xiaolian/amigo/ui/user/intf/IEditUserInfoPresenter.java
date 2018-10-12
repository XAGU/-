package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IEditUserInfoPresenter<v extends IEditUserInfoView> extends IBasePresenter<v> {
    void saveDepartment(String content);

    void saveProfession(String content);

    void saveClass(String content);

    void saveStudentId(String content);
}
