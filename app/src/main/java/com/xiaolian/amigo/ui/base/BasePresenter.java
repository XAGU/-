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

package com.xiaolian.amigo.ui.base;


import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.Error;
import com.xiaolian.amigo.data.network.model.NetworkObserver;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    // 统一管理observer，防止内存泄露
    private CompositeDisposable disposable;
    private V view;

    @Override
    public void onAttach(V view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        view = null;
    }

    public BasePresenter() {
        disposable = new CompositeDisposable();
    }

    @Override
    public void onRemoteInvocationError(Throwable e) {
        // TODO 和mMvpView关联
    }

    @Override
    public void onBizCodeError(Error error) {
        // TODO 和mMvpView关联
    }

    @Override
    public void addObserver(Observable observable, NetworkObserver observer) {
        if (null != disposable) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);

            this.disposable.add(observer);
        }
    }

    public void clearObservers() {
        if (null != disposable && !disposable.isDisposed()) {
            disposable.clear();
        }
    }

    public V getMvpView() {
        return view;
    }

}
