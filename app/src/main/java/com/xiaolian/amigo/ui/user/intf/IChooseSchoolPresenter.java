package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IChooseSchoolPresenter<v extends IChooseSchoolView> extends IBasePresenter<v> {

    void getSchoolNameList();


    void updataSchool(Long id);

    /**
     * 获取学校列表
     *
     * @param page 页数
     * @param size 每页个数
     */
    void getSchoolList(Integer page, Integer size, Boolean online);
}
