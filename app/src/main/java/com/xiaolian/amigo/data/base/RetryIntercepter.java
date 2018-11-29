package com.xiaolian.amigo.data.base;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryIntercepter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (TextUtils.isEmpty(request.url().toString()))  return null ;
        return null;
    }
}
