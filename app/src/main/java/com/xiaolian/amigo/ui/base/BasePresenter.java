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


import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.polidea.rxandroidble.exceptions.BleDisconnectedException;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.data.network.model.Error;
import com.xiaolian.amigo.util.NetworkUtil;

import java.net.ConnectException;

import retrofit2.HttpException;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {
    private static final String TAG = BasePresenter.class.getSimpleName();

    // 统一管理observer，防止内存泄露
    protected CompositeSubscription subscriptions;
    private V view;
    private Handler handler;

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

        HandlerThread thread = new HandlerThread("BasePresenter");
        thread.start();
        handler = new Handler(thread.getLooper());
    }

    @Override
    public void onRemoteInvocationError(Throwable e) {
        if (e instanceof ConnectException) {
            getMvpView().onError("服务器飞走啦，努力修复中");
        }
        else if (e instanceof HttpException) {
            switch (((HttpException) e).code()) {
                case 401:
                    getMvpView().onError(R.string.please_login);
                    getMvpView().redirectToLogin();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBizCodeError(Error error) {
        // TODO 和mMvpView关联
        switch (error.getCode()) {
            // PERMISSION_DENIED(10001, "请重新登录"),
            case 10001:
                getMvpView().onError(R.string.please_login);
                getMvpView().redirectToLogin();
                break;
            // ACCOUNT_EXIST(10002, "该手机号已注册，请直接登录"),
            case 10002:
                getMvpView().onError("该手机号已注册，请直接登录");
                break;
            // SERVER_SYSTEM_ERROR(10003, "服务器错误"),
            case 10003:
                getMvpView().onError("服务器错误");
                break;
            // INVALID_SMS_CODE(10004, "短信验证码输入错误"),
            case 10004:
                getMvpView().onError("短信验证码输入错误");
                break;
            // ACCOUNT_NOT_EXIST(10005,"账号不存在"),
            case 10005:
                getMvpView().onError("账号不存在");
                break;
            // UNMATCHED_PASSWORD(10006,"密码错误" ),
            case 10006:
                getMvpView().onError("密码错误");
                break;
            // ACCOUNT_LOCKED(10007,"账号不可用" ),
            case 10007:
                getMvpView().onError("账号不可用");
                break;
            // BIZ_ERROR(10008, "业务错误"),
            case 10008:
                getMvpView().onError("业务错误");
                break;
            // CLIENT_PARAM_ERROR(10009, "缺少参数"),
            case 10009:
                getMvpView().onError("缺少参数");
                break;
            // INVALID_MOBILE(10010, "手机号不合法:"),
            case 10010:
                getMvpView().onError("手机号不合法");
                break;
            // DATA_NOT_FOUND(10011, "数据不存在:"),
            case 10011:
                getMvpView().onError("数据不存在");
                break;
            // SQL_ERROR(10012, "数据库操作错误"),
            case 10012:
                getMvpView().onError("数据库操作错误");
                break;
            // DUPLICATE_KEY_ERROR(10013, "主键冲突");
            case 10013:
                break;
            default:
                break;
        }
    }

    @Override
    public <P> void addObserver(Observable<P> observable, NetworkObserver observer) {
        if (!getMvpView().isNetworkAvailable()) {
            getMvpView().onError("你的网络不太好哦");
            return;
        }
        if (null != subscriptions) {
            // handler.post(() ->
            this.subscriptions.add(
                    observable
                            .compose(this.applySchedulers())
                            .subscribe(observer));
            // ));
        }
    }

    @Override
    public <P> void addObserver(Observable<P> observable, BLEObserver observer) {
        if (null != subscriptions) {
            // handler.post(() ->
            this.subscriptions.add(
                    observable
                            .compose(this.applySchedulers())
                            .subscribe(observer));
            // ));
        }
    }

    @Override
    public <P> void addObserver(Observable<P> observable, BLEObserver observer, Scheduler scheduler) {
        if (null != subscriptions) {
            //handler.post(() ->
            this.subscriptions.add(
                    observable
                            .subscribeOn(Schedulers.io())
                            .observeOn(scheduler)
                            .subscribe(observer));
            // ));
        }
    }

    private <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public void clearObservers() {
        if (null != subscriptions && !subscriptions.isUnsubscribed()) {
            // handler.post(() -> {
            subscriptions.unsubscribe();
            subscriptions.clear();
            // });
        }
    }

    // 重置订阅列表集合
    public void resetSubscriptions() {
        subscriptions = new CompositeSubscription();
    }

    public V getMvpView() {
        return view;
    }

    public abstract class BLEObserver<T> extends Subscriber<T> {

        @Override
        public void onError(Throwable e) {
            if (e instanceof BleDisconnectedException) {
                onConnectError();
            } else {
                onExecuteError(e);
            }
        }

        // 蓝牙设备连接异常
        public abstract void onConnectError();

        // 执行操作出错
        public abstract void onExecuteError(Throwable e);

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
