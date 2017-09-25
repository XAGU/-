package com.xiaolian.amigo.ui.user;

import android.net.Uri;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryBriefSchoolListRespDTO;
import com.xiaolian.amigo.data.network.model.user.School;
import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.adaptor.ListChooseAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditProfilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfileView;
import com.xiaolian.amigo.util.Constant;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * EditProfilePresenter实现类
 * @author zcd
 */

public class EditProfilePresenter<V extends IEditProfileView> extends BasePresenter<V>
        implements IEditProfilePresenter<V> {

    private static final String TAG = EditProfilePresenter.class.getSimpleName();
    private IUserDataManager manager;

    @Inject
    public EditProfilePresenter(IUserDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void getPersonProfile() {
        addObserver(manager.getUserInfo(), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    getMvpView().setAvatar(result.getData().getPictureUrl());
                    getMvpView().setMobile(result.getData().getMobile());
                    getMvpView().setNickName(result.getData().getNickName());
                    getMvpView().setSchoolName(result.getData().getSchoolName());
                    getMvpView().setResidenceName(result.getData().getResidenceName());
                    if (result.getData().getSex() != null) {
                        getMvpView().setSex(result.getData().getSex());
                    }
                    User user = new User(result.getData());
                    manager.setUser(user);
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void uploadImage(Uri imageUri) {
        RequestBody image = RequestBody.create(MediaType.parse(Constant.UPLOAD_IMAGE_CONTENT_TYPE),
                new File(imageUri.getPath()));
        addObserver(manager.uploadFile(image), new NetworkObserver<ApiResult<String>>() {

            @Override
            public void onReady(ApiResult<String> result) {
                if (null == result.getError()) {
                    getMvpView().showMessage("更换成功");
                    getMvpView().setAvatar(Constant.SERVER + "/images/" + result.getData());
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }

}
