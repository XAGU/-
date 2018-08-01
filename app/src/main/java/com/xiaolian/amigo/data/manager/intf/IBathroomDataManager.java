package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingStatusReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRoomReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRouteRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.CreateBathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.ShowerRoomRouterRespDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;
import rx.Observer;

/**
 * @author zcd
 * @date 18/6/29
 */
public interface IBathroomDataManager {

    /**
     * 获取当前楼栋的浴室房间信息
     */
    Observable<ApiResult<BathBuildingRespDTO>> tree(@Body SimpleReqDTO reqDTO);

    /**
     * 根据当前登录用户所在学校配置，以及用户上次洗澡的习惯，决定路由到"宿舍热水澡模块"、还是"公共浴室模块"
     */
    Observable<ApiResult<BathRouteRespDTO>> route();

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

    /**
     * 获取订单列表
     * @param reqDTO
     * @return
     */
    Observable<ApiResult<QueryBathOrderListRespDTO>> getOrderRecordList(@Body QueryBathOrderListReqDTO reqDTO);


    /**
     * 获取用户Id
     * @return
     */
    Long getUserId();


    /**
     * 预约前操作，获取是否有未完成订单
     * @return
     */
    Observable<ApiResult<BathOrderPreconditionRespDTO>> getOrderPrecondition() ;


    /**
     * 查询指定订单状态
     * @param reqDTO
     * @return
     */
    Observable<ApiResult<BathOrderRespDTO>> query(@Body BathBookingStatusReqDTO reqDTO);

    /**
     * 解除设备绑定
     * @return
     */
    Observable<ApiResult<BooleanRespDTO>> unlock(@Body BathRoomReqDTO reqDTO);


    /**
     * 该方法用于购买洗澡劵前的操作，返回预付信息和红包信息，用户余额，设备详细位置等信息
     */
    Observable<ApiResult<BathPreBookingRespDTO>> preBuyVoucher();
}
