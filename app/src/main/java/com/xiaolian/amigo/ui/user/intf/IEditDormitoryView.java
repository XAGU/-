package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.data.network.model.user.UserResidenceDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
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


    /**
     * 选择完洗澡地址，直接进入公共浴室界面
     *
     * @param dto
     */
    void startBathroom(UserResidenceInListDTO dto);

    /**
     * 选择完洗澡地址， 直接进入热水澡界面
     *
     * @param dto
     */
    void startShower(UserResidenceInListDTO dto);

    /**
     * 设置上一个默认的洗澡地址
     *
     * @param position
     */
    void setLastNormalPosition(int position);

    /**
     * 更新adapter
     *
     * @param currentPosition
     */
    void notifyAdapter(EditDormitoryAdaptor.UserResidenceWrapper wrapper, int currentPosition);

    /**
     * 删除洗澡记录
     *
     * @param position
     */
    void delateBathroomRecord(int position);

    /**
     * 不在供水时间段提示
     *
     * @param title
     * @param remark     提示内容
     * @param pubBath    是否为公共浴室
     * @param residence  洗澡地址
     * @param macAddress mac地址
     */
    void showTimeValidDialog(String title, String remark, boolean pubBath, UserResidenceInListDTO residence, String macAddress);
}
