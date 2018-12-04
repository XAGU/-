package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.login.CancelThirdBindReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IThirdBindPresenter;
import com.xiaolian.amigo.ui.user.intf.IThirdBindView;

import javax.inject.Inject;

import rx.Observable;

public class ThirdBindPresenter<V extends IThirdBindView> extends BasePresenter<V>
        implements IThirdBindPresenter<V> {

    private IUserDataManager userDataManager;

    @Inject
    ThirdBindPresenter(IUserDataManager userDataManager) {
        super();
        this.userDataManager = userDataManager;
    }

    @Override
    public void unbind(int type) {
        Observable<ApiResult<CancelThirdBindReqDTO>> observable = null;
        if (type == EditProfileActivity.ALIPAY_BIND) {
            observable = userDataManager.aplipayUnbind();
        } else if(type == EditProfileActivity.WECHAT_BIND){
            observable = userDataManager.weChatUnbind();
        }

        if (observable != null) {
            addObserver(observable, new NetworkObserver<ApiResult<CancelThirdBindReqDTO>>() {
                @Override
                public void onReady(ApiResult<CancelThirdBindReqDTO> result) {
                    if (null == result.getError()) {
                        if (result.getData().isResult()) {
                            if (type == EditProfileActivity.ALIPAY_BIND) {
                                getMvpView().gotoEditProfile(EditProfileActivity.Event.EventType.UNBIND_ALIPAY);
                            }else if (type == EditProfileActivity.WECHAT_BIND) {
                                getMvpView().gotoEditProfile(EditProfileActivity.Event.EventType.UNBIND_WECHAT);
                            }
                        }
                    } else {
                        getMvpView().onError(result.getError().getDisplayMessage());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    getMvpView().onError("网络出错");
                }
            });
        }
    }
}
