package com.xiaolian.amigo.ui.more;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.manager.intf.IMoreDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.more.intf.IMorePresenter;
import com.xiaolian.amigo.ui.more.intf.IMoreView;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 更多
 *
 * @author zcd
 * @date 17/10/13
 */

public class MorePresenter<V extends IMoreView> extends BasePresenter<V>
        implements IMorePresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = MorePresenter.class.getSimpleName();
    private IMoreDataManager moreDataManager;

    private IMainDataManager mainDataManager ;


    @Inject
    MorePresenter(IMoreDataManager moreDataManager , IMainDataManager mainDataManager) {
        super();
        this.moreDataManager = moreDataManager;
        this.mainDataManager = mainDataManager ;
    }

    @Override
    public void onAttach(V view) {
        super.onAttach(view);
        if (moreDataManager.getTransfer()) {
            getMvpView().showTransfer();
        }
    }

    @Override
    public void logout() {

        addObserver(mainDataManager.revokeToken(), new NetworkObserver<ApiResult<Void>>() {

            @Override
            public void onReady(ApiResult<Void> voidApiResult) {
                moreDataManager.logout();
                getMvpView().onSuccess("退出登录成功");
                getMvpView().redirectToLogin(false , false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                moreDataManager.logout();
                getMvpView().onSuccess("退出登录成功");
                getMvpView().redirectToLogin(false , false);
            }
        });


    }

    @Override
    public String getAccessToken() {
        return moreDataManager.getAccessToken();
    }

    @Override
    public String getRefreshToken() {
        return moreDataManager.getRefreshToken();
    }


    @Override
    public Long getUserId() {
        return moreDataManager.getUserId();
    }

    @Override
    public void deletePushToken() {
        moreDataManager.deletePushToken();
    }

    @Override
    public Long getSchoolId() {
        return moreDataManager.getSchoolId();
    }

    @Override
    public String getPushTag() {
        return moreDataManager.getPushTag();
    }

    @Override
    public void setPushTag(String pushTag) {
        moreDataManager.setPushTag(pushTag);
    }

}
