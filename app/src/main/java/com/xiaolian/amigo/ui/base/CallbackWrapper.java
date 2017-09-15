package com.xiaolian.amigo.ui.base;

import android.util.Log;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

import java.lang.ref.WeakReference;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * 请求包装类
 * @author zcd
 */

public abstract class CallbackWrapper<T extends ApiResult> extends DisposableObserver<T> {
    private static final String TAG = CallbackWrapper.class.getSimpleName();

    private WeakReference<IBaseView> mView;

    public CallbackWrapper(IBaseView view) {
        this.mView = new WeakReference<>(view);
    }

    @Override
    public void onNext(T t) {
        if (t.getError() != null) {
            mView.get().showMessage(t.getError().getDisplayMessage());
            Log.d(TAG, t.getError().getDebugMessage());
        } else {
            onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException exception = (HttpException) e;
            Response response = exception.response();
            Log.d("test", response.code() + "");
            mView.get().showMessage(response.code() + "");
        }
    }

    @Override
    public void onComplete() {

    }


    protected abstract void onSuccess(T t);
}
