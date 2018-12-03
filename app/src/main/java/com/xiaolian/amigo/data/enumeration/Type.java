package com.xiaolian.amigo.data.enumeration;

import java.util.Arrays;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {
    /**
     * 扫描
     */
    SCAN(1),
    /**
     * 连接
     */
    CONNECT(2),
    /**
     * 握手
     */
    SHAKE_HANDS_DEVICE(3),
    /**
     * 开阀
     */
    OPEN(4),
    /**
     * 关阀
     */
    CLOSE(5),
    /**
     * 结账
     */
    CHECKOUT(6),
    /**
     * 预结账(查询账单)
     */
    PRE_CHECK(7),
    /**
     * 推送账单
     */
    PUSH_BILL(8),
    /**
     * 更新key
     */
    UPDATE_KEY(9),
    /**
     * 更新费率
     */
    UPDATE_RATE(10);

    private Integer code;

    private static Type get(Integer code) {
        for (Type type : Type.values()){
            if (type.getCode() == code){
                return type ;
            }
        }
        return null ;
    }
}


