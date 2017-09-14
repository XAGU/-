/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.xiaolian.amigo.ui.login;


import com.xiaolian.amigo.data.manager.ILoginDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.LoginRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginPresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginView;
import com.xiaolian.amigo.util.MessageConstant;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter<V extends ILoginView> extends BasePresenter<V>
        implements ILoginPresenter<V> {

    @Inject
    ILoginDataManager mLoginDataManager;

    @Inject
    public LoginPresenter() {
        super();
    }


    @Override
    public void onLoginClick(String mobile, String password) {
        getMvpView().onError(MessageConstant.PASSWORD_INVALID);
        register("1", "1", "1", "1");
    }

    @Override
    public void register(String code, String mobile, String password, String schoolld) {
        mLoginDataManager.register(code, mobile, password, schoolld)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult<LoginRespDTO>>() {
                    @Override
                    public void accept(@NonNull ApiResult<LoginRespDTO> loginRespDTOApiResult) throws Exception {
                        getMvpView().showMessage(loginRespDTOApiResult.getError().getDebugMessage());
                    }
                }).dispose();
    }
}
