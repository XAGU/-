package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 编辑个人信息View
 * @author zcd
 */
public interface IEditProfileView extends IBaseView {
    void setAvatar(String pictureUrl);

    void setNickName(String nickName);

    void setSex(int sex);

    void setMobile(String mobile);

    void setSchoolName(String schoolName);

    void setResidenceName(String residenceName);

    void gotoChangeMobile();
}
