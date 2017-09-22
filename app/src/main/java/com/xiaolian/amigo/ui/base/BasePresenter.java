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


import android.util.Log;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.Error;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {
    private static final String TAG = BasePresenter.class.getSimpleName();

    // 统一管理observer，防止内存泄露
    protected CompositeSubscription subscriptions;
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
        subscriptions = new CompositeSubscription();
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
    public <P> void addObserver(Observable<P> observable, NetworkObserver observer) {
        if (null != subscriptions) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);

            this.subscriptions.add(observer);
        }
    }

    @Override
    public <P> void addObserver(Observable<P> observable, BLEObserver observer) {
        if (null != subscriptions) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);

            this.subscriptions.add(observer);
        }
    }

    public void clearObservers() {
        if (null != subscriptions && !subscriptions.isUnsubscribed()) {
            subscriptions.clear();
        }
    }

    public V getMvpView() {
        return view;
    }

    public abstract class BLEObserver<T> extends Subscriber<T> {

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onCompleted() {
        }

    }

    public abstract class NetworkObserver<T extends ApiResult> extends Subscriber<T> {

        @Override
        public void onStart() {
            super.onStart();
            view.showLoading();
        }

        @Override
        public void onNext(T t) {
            Error error = t.getError();
            if (null != error) {
                onBizCodeError(error);
            }
            onReady(t);
            view.hideLoading();
        }

        public abstract void onReady(T t);

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, e.getMessage());
            onRemoteInvocationError(e);
            view.hideLoading();
        }

        @Override
        public void onCompleted() {
            view.hideLoading();
        }
    }

}
