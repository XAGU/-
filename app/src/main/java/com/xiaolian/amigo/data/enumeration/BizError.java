package com.xiaolian.amigo.data.enumeration;

/**
 * 业务异常
 *
 * @author zcd
 * @date 17/11/19
 */

public enum BizError {
    INVALID_MOBILE(30001, "手机号不合法"),
    ACCOUNT_EXIST(30002, "该手机号已注册"),
    INVALID_SMS_CODE(30003, "短信验证码错误"),
    ACCOUNT_NOT_EXIST(30004, "账号不存在"),
    UNMATCHED_PASSWORD(30005, "密码错误"),
    ACCOUNT_LOCKED(30006, "账号冻结"),
    SCHOOL_NOT_BOUND(30007, "该学校超出管理范围"),
    DEVICE_EXISTS(30008, "该位置已有设备"),
    RESIDENCE_EXISTS(30009, "位置已存在"),
    DEVICE_MAC_ADDRESS_EXISTS(30010, "设备mac地址重复"),
    RESIDENCE_USED(30011, "请先解绑所有设备"),
    RESIDENCE_NOT_BIND(30012, "请先绑定寝室"),
    LOST_BEYOND_LIMIT(30013, "今日可发布的失物招领信息已经超过10条限额"),
    REDEEM_CODE_INVALID(30014, "兑换码无效"),
    REDEEM_DONE(30015, "你已兑换"),
    REDEEM_BONUS_END(30016, "代金券已被兑换完"),
    SCHOOL_CANNOT_CHANGE(30017, "暂不支持切换学校"),
    BALANCE_NOT_ENOUGH(30018, "余额不足"),
    DEVICE_USEFOR_EXISTS(30019, "水温设置冲突"),
    FUNDS_OPERATION_ERROR(30020, "不是充值订单"),
    NO_VERSION(30021, "当前没有版本信息"),
    WITHDRAW_NOT_VALID(30022, "当前时间无法提现"),
    SCHOOL_CONFIG_NOT_FINISHED(30023, "请先完成学校基础配置"),
    SCHOOL_HAS_BUILDING(30024, "请先删除所有楼栋"),
    ACTIVITY_NOT_EXIST(30025, "你所在学校没有新人有礼活动"),
    BONUS_RECEIVED(30026, "你已领取过代金券"),
    SCHOOL_IS_ONLINE(30027, "请先下线学校"),
    SMS_CODE_SEND_FAILED(30028, "验证码发送失败"),
    COMPLAINT_DUPLICATE(30029, "该账单已有投诉"),
    SCHOOL_BUSINESS_EMPTY(30030, "已上线学校支持功能不能为空"),
    BONUS_DUPLICATE(30031, "已存在一样的代金券"),
    WITHDRAW_LESS_THAN_MIN(30032, "提现金额不能低于10元"),
    REMIND_TOO_OFTEN(30033, "提醒太频繁"),
    DEVICE_BREAKDOWN(30039, "设备已故障");
    private int code;
    private String desc;

    BizError(int code, String desc) {
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
