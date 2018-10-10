package com.xiaolian.amigo.ui.user.intf;

import android.content.Context;
import android.widget.ImageView;

import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.user.UserCertificationActivity;

public interface IUserCertificationPresenter <v extends IUserCertificationView> extends IBasePresenter<v> {


    User getUserInfo();

    void uploadImage(Context activity , String imagePath , int position , OssFileType found );

    void uploadImage(Context activity, String imagePath, OssFileType found , ImageView iv);
}
