package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.user.adaptor.ListChooseAdaptor;

import java.util.List;

/**
 * 列表选择页
 *
 * @author zcd
 * @date 17/9/19
 */

public interface IListChooseView extends IBaseView {
    /**
     * 结束页面
     */
    void finishView();


    /**
     * 返回到宿舍列表
     */
    void backToDormitory();

    /**
     * 返回到报修申请页面
     *
     * @param location 位置
     */
    void backToRepairApply(String location);

    /**
     * 加载列表
     *
     * @param item 列表元素
     */
    void addMore(List<ListChooseAdaptor.Item> item);

    /**
     * 返回到主页
     *
     * @param activitySrc 来源activity
     */
    void backToMain(String activitySrc);

    /**
     * 返回到主页
     */
    void backToMain();

    /**
     * 返回到编辑个人信息
     */
    void backToEditProfile();

    /**
     * 返回到编辑宿舍
     */
    void backToEditDormitory();

    /**
     * 显示空列表
     */
    void showEmptyView();

    /**
     * 隐藏空列表
     */
    void hideEmptyView();

    /**
     * 返回个人信息
     */
    void backToEditProfileActivity(String residenceName);

    /**
     * 选择完洗澡地址，直接进入公共浴室界面
     * @param dto
     */
    void startBathroom(UserResidenceInListDTO dto);

    /**
     * 选择完洗澡地址， 直接进入热水澡界面
     * @param dto
     */
    void startShower(UserResidenceInListDTO dto);


}
