package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 编辑个人信息View
 *
 * @author zcd
 * @date 17/9/15
 */
public interface IEditProfileView extends IBaseView {
    /**
     * 设置头像
     *
     * @param pictureUrl 图片链接
     */
    void setAvatar(String pictureUrl);

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    void setNickName(String nickName);

    /**
     * 设置性别
     *
     * @param sex 性别
     */
    void setSex(int sex);

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    void setMobile(String mobile);

    /**
     * 谁在学校名称
     *
     * @param schoolName 学校名称
     */
    void setSchoolName(String schoolName);

    /**
     * 设置宿舍名称
     *
     * @param residenceName 宿舍名称
     */
    void setResidenceName(String residenceName);

    /**
     * 跳转到修改手机号页面
     */
    void gotoChangeMobile();

    /**
     * 显示修改学校对话框
     */
    void showChangeSchoolDialog();
}
