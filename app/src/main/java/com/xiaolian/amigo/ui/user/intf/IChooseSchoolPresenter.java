package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.widget.school.mode.CityBean;

public interface IChooseSchoolPresenter<v extends IChooseSchoolView> extends IBasePresenter<v> {

    void getSchoolNameList();


    void updataSchool(CityBean cityBean);
    void checkSchool(CityBean cityBean);
    void clearToken(CityBean cityBean);

    /**
     * 获取学校列表
     *
     * @param page 页数
     * @param size 每页个数
     */
    void getSchoolList(Integer page, Integer size, Boolean online);


    void setActionId(int actionId);

    long getCurentSchoolId();
}
