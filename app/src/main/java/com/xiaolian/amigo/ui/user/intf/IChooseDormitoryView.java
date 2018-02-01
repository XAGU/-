package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;

import java.util.List;

/**
 * 宿舍列表
 *
 * @author zcd
 * @date 17/10/11
 */

public interface IChooseDormitoryView extends IBaseListView {
    /**
     * 加载宿舍列表
     *
     * @param wrappers 宿舍
     */
    void addMore(List<EditDormitoryAdaptor.UserResidenceWrapper> wrappers);

    /**
     * 返回到设备页面
     *
     * @param residenceId 位置id
     * @param macAddress  mac地址
     * @param supplierId  供应商id
     * @param location    位置
     */
    void backToDevice(Long residenceId, String macAddress, Long supplierId, String location);
}
