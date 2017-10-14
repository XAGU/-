package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 编辑宿舍
 * <p>
 * Created by zcd on 9/19/17.
 */

public interface IEditDormitoryPresenter<V extends IEditDormitoryView> extends IBasePresenter<V> {
    void queryDormitoryList(int page, int size);

    void deleteDormitory(Long residenceId);

    void updateResidenceId(Long residenceId);

    void saveDefaultResidenceId(Long residenceId);
}
