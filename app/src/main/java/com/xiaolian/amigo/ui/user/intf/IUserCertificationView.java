package com.xiaolian.amigo.ui.user.intf;

import android.widget.ImageView;

import com.xiaolian.amigo.data.network.model.user.UserGradeInfoRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

public interface IUserCertificationView  extends IBaseView {
    void addImage(String filePath, int currentImagePosition, String objectKey);

    void setCardImage(ImageView iv, String filePath, String objectKey);


    void setGradeInfo(UserGradeInfoRespDTO dto);

    void certifySuccess();
}
