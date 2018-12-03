package com.xiaolian.amigo.data.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {
    /**
     * 扫描
     */
    SCAN(1 , "SCAN"),
    /**
     * 连接
     */
    CONNECT(2 , "CONNECT"),
    /**
     * 握手
     */
    SHAKE_HANDS_DEVICE(3 , "SHAKE_HANDS_DEVICE"),
    /**
     * 开阀
     */
    OPEN(4 , "OPEN"),
    /**
     * 关阀
     */
    CLOSE(5 , "CLOSE"),
    /**
     * 结账
     */
    CHECKOUT(6 , "CHECKOUT"),
    /**
     * 预结账(查询账单)
     */
    PRE_CHECK(7 , "PRE_CHECK"),
    /**
     * 推送账单
     */
    PUSH_BILL(8 , "PUSH_BILL"),
    /**
     * 更新key
     */
    UPDATE_KEY(9 , "UPDATE_KEY"),
    /**
     * 更新费率
     */
    UPDATE_RATE(10 , "UPDATE_RATE");

    private Integer code;

    private String content ;

    private static Type get(Integer code) {
        for (Type type : Type.values()){
            if (type.getCode() == code){
                return type ;
            }
        }
        return null ;
    }
}


