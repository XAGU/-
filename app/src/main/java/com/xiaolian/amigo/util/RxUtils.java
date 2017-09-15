package com.xiaolian.amigo.util;

import org.reactivestreams.Subscription;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Rx工具包
 * <p>
 * Created by caidong on 2017/9/15.
 */
public class RxUtils {

    private CompositeDisposable disposable = new CompositeDisposable();

    private RxUtils() {
    }

    private static final class RxUtilsHolder {
        private static final RxUtils RX_UTILS = new RxUtils();
    }

    public static RxUtils getInstance() {
        return RxUtilsHolder.RX_UTILS;
    }

    public void clear() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.clear();
        }
    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void addObserver(DisposableObserver observer) {
        if (disposable != null) {
            disposable.add(observer);
        }
    }

}
