package com.xiaolian.amigo.data.enumeration;

import java.util.Arrays;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Result {
    /**
     * 成功
     */
    SUCCESS(1),
    /**
     * 失败
     */
    FAILED(2),
    /**
     * 重试
     */
    RETRY(3);

    private Integer code;

    private static Result get(Integer code) {
        for (Result  result : Result.values()) {
            if (result.getCode() == code) {
                return result;
            }
        }
        return null ;
    }
}
