package com.xiaolian.amigo.ui.user;

import android.net.Uri;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryAvatarDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.adaptor.EditAvatarAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditAvatarPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditAvatarVIew;
import com.xiaolian.amigo.util.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * <p>
 * Created by zcd on 9/27/17.
 */

public class EditAvatarPresenter<V extends IEditAvatarVIew> extends BasePresenter<V>
        implements IEditAvatarPresenter<V> {

    private IUserDataManager manager;

    @Inject
    public EditAvatarPresenter(IUserDataManager manager) {
        super();
        this.manager = manager;
    }


    @Override
    public void getAvatarList() {
        addObserver(manager.getAvatarList(), new NetworkObserver<ApiResult<QueryAvatarDTO>>() {

            @Override
            public void onReady(ApiResult<QueryAvatarDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getAvatars() != null && result.getData().getAvatars().size() > 0) {
                        List<EditAvatarAdaptor.AvatarWrapper> wrappers = new ArrayList<>();
                        for (String url : result.getData().getAvatars()) {
                            wrappers.add(new EditAvatarAdaptor.AvatarWrapper(url));
                        }
                        getMvpView().addAvatar(wrappers);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
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
                    getMvpView().setAvatar(Constant.SERVER + "/images/" + result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void updateAvatarUrl(String avatarUrl) {
        PersonalUpdateReqDTO dto = new PersonalUpdateReqDTO();
        dto.setPictureUrl(avatarUrl);
        addObserver(manager.updateUserInfo(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {
            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess(R.string.change_success);
                    getMvpView().finishView();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
