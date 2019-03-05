package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;

/**
 * 编辑宿舍
 *
 * @author zcd
 * @date 17/9/19
 */

public interface IEditDormitoryPresenter<V extends IEditDormitoryView> extends IBasePresenter<V> {
    /**
     * 储存默认宿舍id
     *
     * @param residenceId 位置id
     */
    void saveDefaultResidenceId(Long residenceId);

    /**
     * 获取浴室洗澡地址列表
     */
    void queryBathList();

    /**
     * 删除洗澡地址记录
     *
     * @param id
     */
    void deleteBathroomRecord(long id, int position, boolean isDefault);

    /**
     * 更新默认洗澡地址
     *
     * @param
     */
    void updateNormalBathroom(EditDormitoryAdaptor.UserResidenceWrapper userResidenceWrapper, int currentPosition);


}
