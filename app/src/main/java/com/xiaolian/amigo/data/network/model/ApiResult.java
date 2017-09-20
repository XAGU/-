package com.xiaolian.amigo.data.network.model;

/**
 * ApiResult
 * Created by zcd on 9/14/17.
 */

public class ApiResult<T> {
    T data;
    Error error;
    String timestamp;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
