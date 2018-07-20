package com.xiaolian.amigo.ui.notice;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerNoticeActivityComponent;
import com.xiaolian.amigo.di.componet.NoticeActivityComponent;
import com.xiaolian.amigo.di.module.NoticeActivityModule;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;
import com.xiaolian.amigo.ui.base.BaseToolBarListActivity;

/**
 * 通知中心
 *
 * @author zcd
 * @date 17/9/22
 */

public abstract class NoticeBaseActivity extends BaseToolBarActivity {
    private NoticeActivityComponent mActivityComponent;

    @Override
    protected void initInject() {
        mActivityComponent = DaggerNoticeActivityComponent.builder()
                .noticeActivityModule(new NoticeActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    public NoticeActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
