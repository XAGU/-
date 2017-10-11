package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 宿舍列表
 * <p>
 * Created by zcd on 10/11/17.
 */

public interface IChooseDormitoryPresenter<V extends IChooseDormitoryView> extends IBasePresenter<V> {
    void queryDormitoryList(int page, int size);

    void getHeaterDeviceMacAddress(Long residenceId);
}
