package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.CheckVersionUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryDeviceListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryTimeValidReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ReadNotifyReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.CheckVersionUpdateRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryDeviceListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QuerySchoolBizListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryTimeValidRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.VersionDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 主页
 * <p>
 * Created by zcd on 9/22/17.
 */

public interface IMainApi {
    // 用户个人中心额外信息：包括我的钱包余额、我的代金券数量、是否用设备报修中、目前在进行中的订单
    @POST("/user/extraInfo/one")
    Observable<ApiResult<PersonalExtraInfoDTO>> getExtraInfo();

    // 查询热水澡热水供应时间段
    @POST("/time/range/water")
    Observable<ApiResult<QueryTimeValidRespDTO>> queryWaterTimeValid(@Body QueryTimeValidReqDTO reqDTO);

    // 首页设备用水校验
    @POST("/device/check")
    Observable<ApiResult<DeviceCheckRespDTO>> checkDeviceUseage(@Body DeviceCheckReqDTO reqDTO);

    // 告诉服务端通知已读（紧急公告）
    @POST("/notify/read")
    Observable<ApiResult<BooleanRespDTO>> readUrgentNotify(@Body ReadNotifyReqDTO reqDTO);

    // 获取学校业务列表
    @POST("/school/business/list")
    Observable<ApiResult<QuerySchoolBizListRespDTO>> getSchoolBizList();

    // 检查更新
    @POST("/version/check/update")
    Observable<ApiResult<CheckVersionUpdateRespDTO>> checkUpdate(@Body CheckVersionUpdateReqDTO reqDTO);

    // 获取更新信息
    @POST("/version/one")
    Observable<ApiResult<VersionDTO>> getUpdateInfo();
}
