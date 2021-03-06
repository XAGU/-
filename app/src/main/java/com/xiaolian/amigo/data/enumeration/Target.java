package com.xiaolian.amigo.data.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Target {
    /**
     * app与服务器的交互
     */
    SERVER(1 , "SERVER"),
    /**
     * app与设备的交互
     */
    DEVICE(2 , "DEVICE");

    private Integer code;

    private String content ;

    private static Target get(Integer code) {
        for (Target target : Target.values()){
            if (target.getCode() == code){
                return target ;
            }
        }

        return null ;
    }
}
