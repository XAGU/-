package com.xiaolian.amigo.data.enumeration;

/**
 * 协议版本
 *
 * @author zcd
 * @date 17/12/18
 */

public enum AgreementVersion {
    HAONIANHUA(1, "好年华"),
    XINNA(2, "辛纳");
    private int type;
    private String desc;

    AgreementVersion(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static AgreementVersion getAgreement(int type) {
        for (AgreementVersion agreement : AgreementVersion.values()) {
            if (agreement.getType() == type) {
                return agreement;
            }
        }
        return HAONIANHUA;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
