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


import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.login.intf.LoginPresenterIntf;
import com.xiaolian.amigo.ui.login.intf.LoginViewIntf;
import com.xiaolian.amigo.util.MessageConstant;

import javax.inject.Inject;

public class LoginPresenter<V extends LoginViewIntf> extends BasePresenter<V>
        implements LoginPresenterIntf<V> {

    @Inject
    public LoginPresenter() {
        super();
    }


    @Override
    public void onLoginClick(String mobile, String password) {
        getMvpView().onError(MessageConstant.PASSWORD_INVALID);
    }
}
