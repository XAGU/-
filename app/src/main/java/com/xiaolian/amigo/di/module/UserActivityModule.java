package com.xiaolian.amigo.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.xiaolian.amigo.data.manager.UserDataManager;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.di.UserActivityContext;
import com.xiaolian.amigo.ui.user.EditMobilePresenter;
import com.xiaolian.amigo.ui.user.EditNickNamePresenter;
import com.xiaolian.amigo.ui.user.EditPasswordPresenter;
import com.xiaolian.amigo.ui.user.EditProfilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditMobilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditMobileView;
import com.xiaolian.amigo.ui.user.intf.IEditNickNamePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditNickNameView;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordView;
import com.xiaolian.amigo.ui.user.intf.IEditProfilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfileView;

import dagger.Module;
import dagger.Provides;

/**
 * User模块ActivityModule
 * @author zcd
 */
@Module
public class UserActivityModule {

    private AppCompatActivity mActivity;

    public UserActivityModule(AppCompatActivity activity)
    {
        this.mActivity = activity;
    }

    @Provides
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }


    @Provides
    IUserDataManager provideUserDataManager(UserDataManager manager) {
        return manager;
    }

    @Provides
    @UserActivityContext
    IEditProfilePresenter<IEditProfileView> provideEditProfilePresenter(
            EditProfilePresenter<IEditProfileView> presenter) {
        return presenter;
    }

    @Provides
    @UserActivityContext
    IEditNickNamePresenter<IEditNickNameView> provideEditNicknamePresenter(
            EditNickNamePresenter<IEditNickNameView> presenter) {
        return presenter;
    }

    @Provides
    @UserActivityContext
    IEditMobilePresenter<IEditMobileView> provideEditMobilePresenter(
            EditMobilePresenter<IEditMobileView> presenter) {
        return presenter;
    }

    @Provides
    @UserActivityContext
    IEditPasswordPresenter<IEditPasswordView> provideEditPasswordPresenter(
            EditPasswordPresenter<IEditPasswordView> presenter) {
        return presenter;
    }
}
