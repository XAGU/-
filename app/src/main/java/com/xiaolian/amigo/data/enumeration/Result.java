package com.xiaolian.amigo.data.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Result {
    /**
     * 成功
     */
    SUCCESS(1 , "SUCCESS"),
    /**
     * 失败
     */
    FAILED(2 , "FAILED"),
    /**
     * 重试
     */
    RETRY(3 , "RETRY");

    private Integer code;

    private String content ;

    private static Result get(Integer code) {
        for (Result  result : Result.values()) {
            if (result.getCode() == code) {
                return result;
            }
        }
        return null ;
    }
}
