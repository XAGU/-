package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 宿舍列表
 *
 * @author zcd
 * @date 17/10/11
 */

public interface IChooseDormitoryPresenter<V extends IChooseDormitoryView> extends IBasePresenter<V> {
    /**
     * 获取宿舍列表
     *
     * @param page 页数
     * @param size 每页个数
     */
    void queryDormitoryList(int page, int size);
}
