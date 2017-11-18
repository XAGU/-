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
import com.xiaolian.amigo.util.Log;

import com.polidea.rxandroidble.exceptions.BleDisconnectedException;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.data.network.model.Error;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {
    private static final String TAG = BasePresenter.class.getSimpleName();

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int IGNORE = 600;

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
        clearObservers();
        view = null;
    }

    public BasePresenter() {
        subscriptions = new CompositeSubscription();

        HandlerThread thread = new HandlerThread("basepresenter");
        thread.start();
        handler = new Handler(thread.getLooper());
    }

    @Override
    public void onRemoteInvocationError(Throwable e) {
        if (!getMvpView().isNetworkAvailable()) {
            getMvpView().onError("你的网络不太好哦");
        } else if (e instanceof ConnectException) {
            getMvpView().onError("服务器飞走啦，努力修复中");
        } else if (e instanceof SocketTimeoutException) {
            getMvpView().onError("服务器飞走啦，努力修复中");
        } else {
            if (e instanceof HttpException) {
                switch (((HttpException) e).code()) {
                    case UNAUTHORIZED:
                        getMvpView().post(() -> getMvpView().onError(R.string.please_login));
                        getMvpView().post(() -> getMvpView().redirectToLogin());
                        break;
                    case IGNORE:
                        // ignore
                        break;
                    default:
                        getMvpView().onError("服务器飞走啦，努力修复中");
                        break;
                }
            } else {
                Log.wtf(TAG, "sorry，程序上出现错误", e);
                getMvpView().onError("sorry，程序上出现错误");
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

    private void onHttpError(Throwable e) {
        if (e instanceof HttpException) {
            switch (((HttpException) e).code()) {
                case UNAUTHORIZED:
                    getMvpView().post(() -> getMvpView().onError(R.string.please_login));
                    getMvpView().post(() -> getMvpView().redirectToLogin());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public <P> void addObserver(Observable<P> observable, NetworkObserver observer) {
        if (null != subscriptions) {
            this.subscriptions.add(
                    observable
                            .compose(this.applySchedulers())
                            .subscribe(observer));
        }
    }

    @Override
    public <P> void addObserver(Observable<P> observable, NetworkObserver observer, Scheduler scheduler) {
        if (null != subscriptions) {
            this.subscriptions.add(
                    observable
                            .subscribeOn(Schedulers.io())
                            .observeOn(scheduler)
                            .subscribe(observer));
        }
    }

    @Override
    public <P> void addObserver(Observable<P> observable, BleObserver observer) {
        if (null != subscriptions) {
            this.subscriptions.add(
                    observable
                            .compose(this.applySchedulers())
                            .subscribe(observer));
        }
    }

    @Override
    public <P> void addObserver(Observable<P> observable, BleObserver observer, Scheduler scheduler) {
        if (null != subscriptions) {
            this.subscriptions.add(
                    observable
                            .subscribeOn(Schedulers.io())
                            .observeOn(scheduler)
                            .subscribe(observer));
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
        clearObservers();
        subscriptions = new CompositeSubscription();
    }

    public V getMvpView() {
        return view;
    }

    public abstract class BleObserver<T> extends Subscriber<T> {

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

        private boolean renderView = true;
        // 用于列表页token失效后跳转到登录页
        private boolean checkHttpError = false;

        public NetworkObserver(boolean renderView) {
            this.renderView = renderView;
        }

        public NetworkObserver(boolean renderView, boolean checkHttpError) {
            this.renderView = renderView;
            this.checkHttpError = checkHttpError;
        }

        public NetworkObserver() {

        }

        @Override
        public void onStart() {
            super.onStart();
            if (renderView) {
                view.showLoading();
            }
        }

        @Override
        public void onNext(T t) {
            Error error = t.getError();
            if (null != error) {
                getMvpView().post(() -> onBizCodeError(error));
            }
            onReady(t);
            if (renderView) {
                view.hideLoading();
            }
        }

        public abstract void onReady(T t);

        @Override
        public void onError(Throwable e) {
            try {
                Log.d(TAG, e.getMessage());
                if (renderView) {
                    onRemoteInvocationError(e);
                    view.hideLoading();
                }
                if (checkHttpError) {
                    // 处理toke失效需要重新登录的错误
                    onHttpError(e);
                }
            } catch (Exception e1) {
                Log.wtf(TAG, e1);
            }
        }

        @Override
        public void onCompleted() {
            if (renderView) {
                view.hideLoading();
            }
        }
    }


}
