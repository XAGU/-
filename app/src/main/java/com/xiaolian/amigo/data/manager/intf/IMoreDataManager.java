package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import rx.Observable;

/**
 * 更多
 *
 * @author zcd
 * @date 17/10/13
 */

public interface IMoreDataManager {
    void logout();

    boolean getTransfer();

    Long getUserId();

    void deletePushToken();

    Long getSchoolId();

    String getPushTag();

    void setPushTag(String pushTag);

    String getAccessToken();

    String getRefreshToken();

    String getMobile();


    /**
     * 上传错误日志
     */
    Observable<ApiResult<BooleanRespDTO>> uploadLog(@Body RequestBody body);
}
