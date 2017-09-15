package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.util.RxUtils;

import org.reactivestreams.Subscriber;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 基础管理类
 * <p>
 * Created by caidong on 2017/9/15.
 */
public class BaseManager {

    // 添加网络请求事件观察者
    static <M> void addObservable(Observable<M> observable, DisposableObserver<M> observer) {

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        RxUtils.getInstance().addObserver(observer);
    }
}
