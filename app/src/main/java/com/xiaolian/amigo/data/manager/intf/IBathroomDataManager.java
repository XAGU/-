package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.ShowerRoomRouterRespDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;

import retrofit2.http.Body;
import rx.Observable;

/**
 * @author zcd
 * @date 18/6/29
 */
public interface IBathroomDataManager {

    /**
     * 获取当前楼栋的浴室房间信息
     */
    Observable<ApiResult<BathBuildingRespDTO>> list(@Body SimpleReqDTO reqDTO);

    /**
     * 根据当前登录用户所在学校配置，以及用户上次洗澡的习惯，决定路由到"宿舍热水澡模块"、还是"公共浴室模块"
     */
    Observable<ApiResult<ShowerRoomRouterRespDTO>> route();

    /**
     * 该方法用于锁定指定设备,以及返回预付信息和红包信息，用户余额，设备详细位置，失约次数，支付过期时间等
     */
    Observable<ApiResult<BathPreBookingRespDTO>> preBooking(@Body BathBookingReqDTO reqDTO);

    /**
     * 用户支付预约或者购买编码
     */
    Observable<ApiResult<BathOrderRespDTO>> pay(@Body BathOrderReqDTO reqDTO);

    /**
     * 用户取消预约或者购买的编码
     */
    Observable<ApiResult<BooleanRespDTO>> cancel(@Body SimpleReqDTO reqDTO);
}
