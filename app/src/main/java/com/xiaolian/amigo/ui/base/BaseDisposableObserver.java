package com.xiaolian.amigo.ui.base;

import android.util.Log;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * BaseDisposableObserver
 * @author zcd
 */

public abstract class BaseDisposableObserver<T> extends DisposableObserver<T> {

    private IBaseView mView;

    public BaseDisposableObserver(IBaseView view) {
        mView = view;
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException exception = (HttpException) e;
            Response response = exception.response();
            Log.d("test", response.code() + "");
            mView.showMessage(response.code() + "");
        }

    }
}
