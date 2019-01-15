package com.xiaolian.amigo.data.enumeration;

/**
 * 通用异常
 *
 * @author zcd
 * @date 17/11/19
 */

public enum CommonError {
    PERMISSION_DENIED(10001, "请重新登录"),
    SERVER_SYSTEM_ERROR(10002, "服务器错误"),
    CLIENT_PARAM_ERROR(10003, "参数异常"),
    NO_ACCESS(10004, "没有权限"),
    DATA_NOT_EXIST(10005, "数据不存在"),
    DATA_EXIST(10006, "数据已存在"),
    SQL_ERROR(10007, "数据库操作错误"),
    DUPLICATE_KEY_ERROR(10008, "主键冲突"),
    STATUS_ERROR(10009, "状态异常"),
    AES_ENCODE_ERROR(10010, "AES加密出错"),
    AES_DECODE_ERROR(10011, "AES解密出错"),
    ANOTHER_DEVICE_LOGIN(10021 ,"你的账号在另一台设备登录，如非本人操作请及时修改密码");
    private int code;
    private String desc;

    CommonError(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
