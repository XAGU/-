package com.xiaolian.amigo.util;


import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * @author  wcm
 * rxjava
 */
public class RxHelper {

    /**
     * 定时发送
     */
    public static Observable<Integer> countDown(int time){
        if (time < 0 ) time =0   ;
        final  int countTime = time ;
        return Observable.interval(0 , 1 , TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        return countTime - aLong.intValue();
                    }
                }).take(countTime + 1);
    }
}
