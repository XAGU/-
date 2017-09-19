package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 列表选择页
 * <p>
 * Created by zcd on 9/19/17.
 */

public interface IListChoosePresenter<V extends IListChooseView> extends IBasePresenter<V> {

    void getSchoolList(Integer page, Integer size);

    void getBuildList(Integer page, Integer size);

    void updateSchool(Integer schoolId);

    void updateSex(int sex);

    void getFloorList(int page, int size, int parentId);

    void bindDormitory(Integer id, boolean isEdit);
}
