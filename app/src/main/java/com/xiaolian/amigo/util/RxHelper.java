package com.xiaolian.amigo.util;


import android.widget.ImageView;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
                .map(aLong -> countTime - aLong.intValue()).take(countTime + 1);
    }

    /**
     * 延迟发送
     * @param time
     * @param
     * @return
     */
    public static Subscription delay(int time , Action1<Long> action1){
        if (time < 0) time = 0 ;
        return Observable.timer( time ,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    public static Observable<Integer> delay(int time ,TimeUnit timeUnit){
        if (time < 0) time = 0 ;
        return Observable.just(time).delay(time ,timeUnit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
