package com.xiaolian.amigo.data.network.model;

/**
 * ApiResult
 *
 * @author zcd
 * @date 17/9/14
 */

public class ApiResult<T> {
    T data;
    Error error;
    Long timestamp;

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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
