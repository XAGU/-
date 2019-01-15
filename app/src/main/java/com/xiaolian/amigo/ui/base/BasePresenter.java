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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xiaolian.amigo.BuildConfig;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.BizError;
import com.xiaolian.amigo.data.enumeration.CommonError;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.Error;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.crash.acra.util.JsonUtils;

import java.io.IOException;
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
    protected  final String TAG = this.getClass().getSimpleName();

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
    public CompositeSubscription subscriptions;
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
            getMvpView().onError(R.string.network_not_available);
//            getMvpView().onError(R.string.network_available_error_tip);
        } else if (e instanceof ConnectException) {
//            getMvpView().onError("服务器开小差了>_<");
        } else if (e instanceof SocketTimeoutException) {
//            getMvpView().onError("服务器开小差了>_<");
        } else {
            if (e instanceof HttpException) {
                switch (((HttpException) e).code()) {
                    case UNAUTHORIZED:
                        http401((HttpException) e);
                        break;
                    case IGNORE:
                        // ignore
                        break;
                    default:
//                        getMvpView().onError("服务器开小差了>_<");
                        break;
                }
            } else if (e instanceof IOException) {
//                getMvpView().onError(R.string.network_available_error_tip);
            } else {
                Log.wtf(TAG, "sorry，程序上出现错误", e);
                if (e instanceof NullPointerException
                        && "prod".equals(BuildConfig.ENVIRONMENT)
                        && "release".equals(BuildConfig.BUILD_TYPE)) {
                    return;
                }
                getMvpView().onError("sorry，程序上出现错误");
            }
        }
    }

    @Override
    public void onBizCodeError(Error error) {
        // TODO 和mMvpView关联
        int errorCode = error.getCode();
        // 处理通用异常
        if (errorCode == CommonError.PERMISSION_DENIED.getCode()) {
            Log.w(TAG, "请重新登录");
            getMvpView().onError(CommonError.PERMISSION_DENIED.getDesc());
            getMvpView().redirectToLogin(false);
        }else if (errorCode == CommonError.ANOTHER_DEVICE_LOGIN.getCode()){
            getMvpView().onError(CommonError.ANOTHER_DEVICE_LOGIN.getDesc());
            getMvpView().redirectToLogin(true);
        } else if (errorCode == CommonError.SERVER_SYSTEM_ERROR.getCode()) {
//            Log.w(TAG, "服务器错误");
//            getMvpView().onError("服务器开小差了>_<");
        } else if (errorCode == CommonError.CLIENT_PARAM_ERROR.getCode()) {
            Log.w(TAG, "参数异常");
        } else if (errorCode == CommonError.NO_ACCESS.getCode()) {
            Log.w(TAG, "没有权限");
        } else if (errorCode == CommonError.DATA_NOT_EXIST.getCode()) {
            Log.w(TAG, "数据不存在");
        } else if (errorCode == CommonError.DATA_EXIST.getCode()) {
            Log.w(TAG, "数据已存在");
        } else if (errorCode == CommonError.SQL_ERROR.getCode()) {
            Log.w(TAG, "数据库操作错误");
        } else if (errorCode == CommonError.DUPLICATE_KEY_ERROR.getCode()) {
            Log.w(TAG, "主键冲突");
        } else if (errorCode == CommonError.STATUS_ERROR.getCode()) {
            Log.w(TAG, "状态异常");
        } else if (errorCode == CommonError.AES_ENCODE_ERROR.getCode()) {
            Log.w(TAG, "AES加密出错");
        } else if (errorCode == CommonError.AES_DECODE_ERROR.getCode()) {
            Log.w(TAG, "AES解密出错");
        }

        // 处理业务异常
        if (errorCode == BizError.INVALID_MOBILE.getCode()) {
            Log.w(TAG, "手机号不合法");
        } else if (errorCode == BizError.ACCOUNT_EXIST.getCode()) {
            Log.w(TAG, "该手机号已注册");
        } else if (errorCode == BizError.INVALID_SMS_CODE.getCode()) {
            Log.w(TAG, "短信验证码错误");
        } else if (errorCode == BizError.ACCOUNT_NOT_EXIST.getCode()) {
            Log.w(TAG, "账号不存在");
        } else if (errorCode == BizError.UNMATCHED_PASSWORD.getCode()) {
            Log.w(TAG, "密码错误");
        } else if (errorCode == BizError.ACCOUNT_LOCKED.getCode()) {
            Log.w(TAG, "账号冻结");
        } else if (errorCode == BizError.SCHOOL_NOT_BOUND.getCode()) {
            Log.w(TAG, "该学校超出管理范围");
        } else if (errorCode == BizError.DEVICE_EXISTS.getCode()) {
            Log.w(TAG, "该位置已有设备");
        } else if (errorCode == BizError.RESIDENCE_EXISTS.getCode()) {
            Log.w(TAG, "位置已存在");
        } else if (errorCode == BizError.DEVICE_MAC_ADDRESS_EXISTS.getCode()) {
            Log.w(TAG, "备mac地址重复");
        } else if (errorCode == BizError.RESIDENCE_USED.getCode()) {
            Log.w(TAG, "请先解绑所有设备");
        } else if (errorCode == BizError.RESIDENCE_NOT_BIND.getCode()) {
            Log.w(TAG, "请先绑定寝室");
        } else if (errorCode == BizError.LOST_BEYOND_LIMIT.getCode()) {
            Log.w(TAG, "今日可发布的失物招领信息已经超过10条限额");
        } else if (errorCode == BizError.REDEEM_CODE_INVALID.getCode()) {
            Log.w(TAG, "兑换码无效");
        } else if (errorCode == BizError.REDEEM_DONE.getCode()) {
            Log.w(TAG, "你已兑换");
        } else if (errorCode == BizError.REDEEM_BONUS_END.getCode()) {
            Log.w(TAG, "代金券已被兑换完");
        } else if (errorCode == BizError.SCHOOL_CANNOT_CHANGE.getCode()) {
            Log.w(TAG, "暂不支持切换学校");
        } else if (errorCode == BizError.BALANCE_NOT_ENOUGH.getCode()) {
            Log.w(TAG, "余额不足");
        } else if (errorCode == BizError.DEVICE_USEFOR_EXISTS.getCode()) {
            Log.w(TAG, "水温设置冲突");
        } else if (errorCode == BizError.FUNDS_OPERATION_ERROR.getCode()) {
            Log.w(TAG, "不是充值订单");
        } else if (errorCode == BizError.NO_VERSION.getCode()) {
            Log.w(TAG, "当前没有版本信息");
        } else if (errorCode == BizError.WITHDRAW_NOT_VALID.getCode()) {
            Log.w(TAG, "当前时间无法提现");
        } else if (errorCode == BizError.SCHOOL_CONFIG_NOT_FINISHED.getCode()) {
            Log.w(TAG, "请先完成学校基础配置");
        } else if (errorCode == BizError.SCHOOL_HAS_BUILDING.getCode()) {
            Log.w(TAG, "请先删除所有楼栋");
        } else if (errorCode == BizError.ACTIVITY_NOT_EXIST.getCode()) {
            Log.w(TAG, "你所在学校没有新人有礼活动");
        } else if (errorCode == BizError.BONUS_RECEIVED.getCode()) {
            Log.w(TAG, "你已领取过代金券");
        } else if (errorCode == BizError.SCHOOL_IS_ONLINE.getCode()) {
            Log.w(TAG, "请先下线学校");
        } else if (errorCode == BizError.SMS_CODE_SEND_FAILED.getCode()) {
            Log.w(TAG, "验证码发送失败");
        } else if (errorCode == BizError.COMPLAINT_DUPLICATE.getCode()) {
            Log.w(TAG, "该账单已有投诉");
        } else if (errorCode == BizError.SCHOOL_BUSINESS_EMPTY.getCode()) {
            Log.w(TAG, "已上线学校支持功能不能为空");
        } else if (errorCode == BizError.BONUS_DUPLICATE.getCode()) {
            Log.w(TAG, "已存在一样的代金券");
        } else if (errorCode == BizError.WITHDRAW_LESS_THAN_MIN.getCode()) {
            Log.w(TAG, "提现金额不能低于10元");
        } else if (errorCode == BizError.REMIND_TOO_OFTEN.getCode()) {
            Log.w(TAG, "提醒太频繁");
        }

    }

    private void onHttpError(Throwable e) {
        if (e instanceof HttpException) {
            switch (((HttpException) e).code()) {
                case UNAUTHORIZED:
                    http401((HttpException) e);
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
                if (view != null) view.showLoading();
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
                Log.wtf(TAG, e);
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

    /**
     * 401 异常中分辨是未登录异常和账号异常登录异常
     * @param e
     */
    public void http401(HttpException e) {

        try {
            String errorBody = ((HttpException) e).response().errorBody().string();
            Gson gson = new Gson();
            ApiResult result = gson.fromJson(errorBody, ApiResult.class);
            if (result.getError() != null) {
                if (result.getError().getCode() == CommonError.ANOTHER_DEVICE_LOGIN.getCode()) {
                    getMvpView().post(() -> getMvpView().redirectToLogin(true));
                } else {
                    getMvpView().post(() -> getMvpView().onError(R.string.please_login));
                    getMvpView().post(() -> getMvpView().redirectToLogin(false));
                }
            }
        } catch (IOException e1) {
            getMvpView().post(() -> getMvpView().onError(R.string.please_login));
            getMvpView().post(() -> getMvpView().redirectToLogin(false));
            android.util.Log.e(TAG, "http401: " + e.getMessage() );
        }
    }

}
