package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathRouteRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.CurrentBathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.ShowerRoomRouterRespDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCategoryBO;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.data.network.model.user.QueryUserResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.timerange.QueryTimeValidReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.version.VersionDTO;
import com.xiaolian.amigo.data.network.model.notify.ReadNotifyReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.system.BaseInfoDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolBizListRespDTO;
import com.xiaolian.amigo.data.network.model.timerange.QueryTimeValidRespDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.user.UploadUserDeviceInfoReqDTO;
import com.xiaolian.amigo.data.vo.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Body;
import rx.Observable;

/**
 * 主页
 *
 * @author zcd
 * @date 17/9/20
 */

public interface IMainDataManager {

    String getToken();

    void setToken(String token);

    User getUserInfo();

    void setUserInfo(User user);

    void logout();

    boolean isShowUrgencyNotify();

    void setShowUrgencyNotify(boolean isShow);

    void setBonusAmount(int amount);

    int getBonusAmount();

    void setBalance(String balance);

    String getBalance();

    /**
     * 获取个人订单
     */
    Observable<ApiResult<OrderRespDTO>> queryOrders(@Body OrderReqDTO reqDTO);

    /**
     * 基础信息
     */
    Observable<ApiResult<BaseInfoDTO>> getSystemBaseInfo();

    /**
     * 版本更新查询
     */
    Observable<ApiResult<CheckVersionUpdateRespDTO>> checkUpdate(@Body CheckVersionUpdateReqDTO reqDTO);

    void setLastUpdateRemindTime();

    Long getLastUpdateRemindTime();

    Integer getMainGuide();

    void setMainGuide(Integer guideTime);

    void setLastRepairTime(Long time);

    Long getLastRepairTime();

    /**
     * 提交设备信息
     */
    Observable<ApiResult<BooleanRespDTO>> uploadDeviceInfo(@Body UploadUserDeviceInfoReqDTO reqDTO);

    void saveUploadedUserDeviceInfo(UploadUserDeviceInfoReqDTO reqDTO);

    UploadUserDeviceInfoReqDTO getUploadedUserDeviceInfo();

    /**
     * 用户个人中心额外信息：包括我的钱包余额、我的代金券数量、是否用设备报修中、目前在进行中的订单
     */
    Observable<ApiResult<PersonalExtraInfoDTO>> getExtraInfo();

    /**
     * 查询热水澡热水供应时间段
     */
    Observable<ApiResult<QueryTimeValidRespDTO>> queryWaterTimeValid(@Body QueryTimeValidReqDTO reqDTO);

    /**
     * 首页设备用水校验
     */
    Observable<ApiResult<DeviceCheckRespDTO>> checkDeviceUseage(@Body DeviceCheckReqDTO reqDTO);

    /**
     * 告诉服务端通知已读（紧急公告）
     */
    Observable<ApiResult<BooleanRespDTO>> readUrgentNotify(@Body ReadNotifyReqDTO reqDTO);

    /**
     * 获取学校业务列表
     */
    Observable<ApiResult<QuerySchoolBizListRespDTO>> getSchoolBizList();

    /**
     * 获取更新信息
     */
    Observable<ApiResult<VersionDTO>> getUpdateInfo();

    void saveDeviceCategory(List<DeviceCategoryBO> devices);

    /**
     * 是否需要账户迁移
     */
    void setNeedTransfer();

    void setNotNeedTransfer();

    /**
     * 缓存积分
     * @param credits 积分
     */
    void setCredits(Integer credits);

    /**
     * 获取缓存中的积分
     * @return 积分
     */
    Integer getCredits();

    void setSchoolBiz(List<BriefSchoolBusiness> businesses);
    List<BriefSchoolBusiness> getSchoolBiz();

    void deletePushToken();

    String getPushToken();

    void setPushToken(String pushToken);

    /**
     * 根据当前登录用户所在学校配置，以及用户上次洗澡的习惯，决定路由到"宿舍热水澡模块"、还是"公共浴室模块"
     */
    Observable<ApiResult<BathRouteRespDTO>> route();

    void setBathroomPasswordDesc(ArrayList<String> bathPasswordDescription);

    /**
     * 主页上显示是否有上一次订单
     * @return
     */
    Observable<ApiResult<CurrentBathOrderRespDTO>> currentOrder();

    /**
     * 获取评论是否开启
     * @return
     */
    boolean getCommentEnable();

    boolean getIsFirstAfterLogin();

    void setIsFirstAfterLogin(boolean b);
}
