package com.xiaolian.amigo.ui.more;

import com.xiaolian.amigo.data.manager.intf.IMoreDataManager;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.more.intf.IMorePresenter;
import com.xiaolian.amigo.ui.more.intf.IMoreView;

import javax.inject.Inject;

/**
 * 更多
 * <p>
 * Created by zcd on 10/13/17.
 */

public class MorePresenter<V extends IMoreView> extends BasePresenter<V>
        implements IMorePresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = MorePresenter.class.getSimpleName();
    private IMoreDataManager moreDataManager;

    @Inject
    public MorePresenter(IMoreDataManager moreDataManager) {
        super();
        this.moreDataManager = moreDataManager;
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
}
