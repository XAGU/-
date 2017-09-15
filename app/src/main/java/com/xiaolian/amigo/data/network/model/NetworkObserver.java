package com.xiaolian.amigo.data.network.model;

import io.reactivex.observers.DisposableObserver;

/**
 * 网络请求观察者
 * <p>
 * Created by caidong on 2017/9/15.
 */
public abstract class NetworkObserver<T> extends DisposableObserver<T> {

    @Override
    public void onStart() {
        super.onStart();
    }

}
