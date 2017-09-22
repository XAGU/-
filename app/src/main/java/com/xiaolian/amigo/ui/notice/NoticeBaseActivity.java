package com.xiaolian.amigo.ui.notice;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerNoticeActivityComponent;
import com.xiaolian.amigo.di.componet.NoticeActivityComponent;
import com.xiaolian.amigo.di.module.NoticeActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;

/**
 * 通知中心
 * <p>
 * Created by zcd on 9/22/17.
 */

public abstract class NoticeBaseActivity extends BaseActivity {
    private NoticeActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        mActivityComponent = DaggerNoticeActivityComponent.builder()
                .noticeActivityModule(new NoticeActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public NoticeActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
