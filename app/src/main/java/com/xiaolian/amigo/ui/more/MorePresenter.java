package com.xiaolian.amigo.ui.more;

import com.xiaolian.amigo.data.manager.intf.IMoreDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.more.intf.IMorePresenter;
import com.xiaolian.amigo.ui.more.intf.IMoreView;

import javax.inject.Inject;

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

    @Inject
    MorePresenter(IMoreDataManager moreDataManager) {
        super();
        this.moreDataManager = moreDataManager;
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
        moreDataManager.logout();
        getMvpView().onSuccess("退出登录成功");
        getMvpView().backToMain();
        getMvpView().redirectToLogin();
    }

    @Override
    public String getToken() {
        return moreDataManager.getToken();
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
}
