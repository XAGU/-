package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.QueryAvatarDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.adaptor.EditAvatarAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditAvatarPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditAvatarVIew;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
}
