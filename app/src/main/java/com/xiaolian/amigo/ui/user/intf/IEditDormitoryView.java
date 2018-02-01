package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.data.network.model.user.UserResidenceDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;

import java.util.List;

/**
 * 编辑宿舍
 *
 * @author zcd
 * @date 17/9/19
 */

public interface IEditDormitoryView extends IBaseListView {
    /**
     * 加载宿舍列表
     *
     * @param userResidenceWrappers 宿舍
     */
    void addMore(List<EditDormitoryAdaptor.UserResidenceWrapper> userResidenceWrappers);

    /**
     * 更新列表
     */
    void notifyAdaptor();

    /**
     * 更新默认宿舍
     *
     * @param defaultId id
     */
    void refreshList(Long defaultId);

    /**
     * 编辑宿舍
     *
     * @param id       id
     * @param data     宿舍信息
     * @param position 列表位置
     */
    void editDormitory(Long id, UserResidenceDTO data, int position);
}
