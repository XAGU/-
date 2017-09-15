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


import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.ILoginDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginPresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginView;

import javax.inject.Inject;

public class LoginPresenter<V extends ILoginView> extends BasePresenter<V>
        implements ILoginPresenter<V> {

    private ILoginDataManager mLoginDataManager;

    @Inject
    public LoginPresenter(ILoginDataManager manager) {
        super();
        mLoginDataManager = manager;
    }


    @Override
    public void onLoginClick(String mobile, String password) {
        getMvpView().onError(R.string.password_invalid);
        register("1", 1, "1", 1);
    }

    @Override
    public void register(String code, int mobile, String password, int schoolld) {
//        mLoginDataManager.register(new RegisterReqDTO(code, mobile, password, schoolld))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Response<ApiResult<LoginRespDTO>>>() {
//                    @Override
//                    public void accept(@NonNull Response<ApiResult<LoginRespDTO>> apiResultResponse) throws Exception {
//                        getMvpView().showMessage(apiResultResponse.body().getError().getDebugMessage() + "");
//                        Log.d("test", "onNext:" + apiResultResponse.code());
//                    }
//                });
//                .subscribe(new DisposableSubscriber<ApiResult<LoginRespDTO>>() {
//
//                    @Override
//                    public void onNext(ApiResult<LoginRespDTO> apiResultResponse) {
////                        getMvpView().showMessage(apiResultResponse.body().getError().getDebugMessage() + "");
//                        Log.d("test", "onNext:");
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        if (t instanceof HttpException) {
//                            HttpException exception = (HttpException) t;
//                            Response response = exception.response();
//                            Log.d("test", response.code() + "");
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }
}
