package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 编辑宿舍
 *
 * @author zcd
 * @date 17/9/19
 */

public interface IEditDormitoryPresenter<V extends IEditDormitoryView> extends IBasePresenter<V> {
    /**
     * 获取宿舍列表
     *
     * @param page 页数
     * @param size 每页个数
     */
    void queryDormitoryList(int page, int size);

    /**
     * 删除宿舍
     *
     * @param residenceId 位置id
     */
    void deleteDormitory(Long residenceId);

    /**
     * 更新宿舍id
     *
     * @param residenceId 位置id
     */
    void updateResidenceId(Long residenceId);

    /**
     * 储存默认宿舍id
     *
     * @param residenceId 位置id
     */
    void saveDefaultResidenceId(Long residenceId);

    /**
     * 获取宿舍详情
     *
     * @param id       位置id
     * @param position 列表位置
     */
    void queryDormitoryDetail(Long id, int position);
}
