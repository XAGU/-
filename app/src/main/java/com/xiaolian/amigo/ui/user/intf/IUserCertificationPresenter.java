package com.xiaolian.amigo.ui.user.intf;

import android.content.Context;
import android.widget.ImageView;

import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.user.UserCertificationActivity;

import java.io.File;
import java.util.List;

public interface IUserCertificationPresenter <v extends IUserCertificationView> extends IBasePresenter<v> {


    User getUserInfo();

    void uploadImage(Context activity , String imagePath , int position , OssFileType found );

    void uploadImage(Context activity, String imagePath, OssFileType found , ImageView iv);

    String getDormitory();

    void getGradeInfo();

    void certify(String className , String faculty , Integer grade , File idCardBehind ,
                 File idCardFront , String major , Long stuNum , List<File> stuPictureUrls);

    void setCertifyStatus(int certificationReviewing);
}
