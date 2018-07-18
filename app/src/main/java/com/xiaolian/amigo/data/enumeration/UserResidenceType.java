package com.xiaolian.amigo.data.enumeration;

/**
 * 洗澡地址类型：1 宿舍洗澡地址 2 公共浴室洗澡地址
 *
 * @author zcd
 * @date 18/7/17
 */
public enum UserResidenceType {
    ROOM(1), BATH(2);

    UserResidenceType(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public static UserResidenceType getUserResidenceType(int code) {
        for (UserResidenceType userResidenceType : UserResidenceType.values()) {
            if (userResidenceType.getCode() == code) {
                return userResidenceType;
            }
        }
        return null;
    }
}
