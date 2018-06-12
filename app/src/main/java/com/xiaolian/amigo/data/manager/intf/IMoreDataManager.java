package com.xiaolian.amigo.data.manager.intf;

/**
 * 更多
 *
 * @author zcd
 * @date 17/10/13
 */

public interface IMoreDataManager {
    void logout();

    String getToken();

    boolean getTransfer();

    Long getUserId();

    void deletePushToken();

    Long getSchoolId();

    String getPushTag();

    void setPushTag(String pushTag);
}
