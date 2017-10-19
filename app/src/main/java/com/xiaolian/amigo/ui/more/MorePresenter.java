package com.xiaolian.amigo.ui.more;

import com.xiaolian.amigo.data.manager.intf.IMoreDataManager;
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

    private static final String TAG = MorePresenter.class.getSimpleName();
    private IMoreDataManager manager;

    @Inject
    public MorePresenter(IMoreDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void logout() {
        manager.logout();
        getMvpView().onSuccess("退出登录成功");
        getMvpView().backToMain();
        getMvpView().redirectToLogin();
    }
}
