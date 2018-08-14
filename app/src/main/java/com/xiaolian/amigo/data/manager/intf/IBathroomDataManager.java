package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingStatusReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderCurrentRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRoomReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRouteRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BookingQueueProgressDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BuildingTrafficDTO;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.TryBookingResultRespDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

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
     * 用户取消预约或者购买的编码
     */
    Observable<ApiResult<BathOrderRespDTO>> cancel(@Body SimpleReqDTO reqDTO);

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
    Observable<ApiResult<BathBookingRespDTO>> query(@Body BathBookingStatusReqDTO reqDTO);


    /**
     * 查询指定订单状态
     * @param reqDTO
     * @return
     */
    Observable<ApiResult<BathOrderCurrentRespDTO>> orderQuery(@Body SimpleReqDTO reqDTO);

    /**
     * 解除设备绑定
     * @return
     */
    Observable<ApiResult<BooleanRespDTO>> unlock(@Body BathRoomReqDTO reqDTO);


    /**
     * 该方法用于购买洗澡劵前的操作，返回预付信息和红包信息，用户余额，设备详细位置等信息
     */
    Observable<ApiResult<BathPreBookingRespDTO>> preBuyVoucher();


    /**
     * 预约设备
     */
    Observable<ApiResult<TryBookingResultRespDTO>> booking(@Body BathBookingReqDTO reqDTO);


    /**
     * 客户端请求结账
     */
    Observable<ApiResult<BooleanRespDTO>> askSettle(SimpleReqDTO reqDTO);


    /**
     * 获取是否有公共浴室密码
     */
    boolean getBathroomPassword();

    /**
     * 查询每个楼层的使用流量
     */
    Observable<ApiResult<BuildingTrafficDTO>>  traffi(@Body SimpleReqDTO reqDTO) ;


    /**
     * 查询指定排队订单的信息
     * @param reqDTO
     * @return
     */
    Observable<ApiResult<BookingQueueProgressDTO>> queueQuery(@Body SimpleReqDTO reqDTO);


    /**
     * 取消预约排队
     * @param reqDTO
     * @return
     */
    Observable<ApiResult<BooleanRespDTO>> cancelQueue(@Body SimpleReqDTO reqDTO);



    /**
     * 提醒服务器超时
     * @return
     */
    Observable<ApiResult<BooleanRespDTO>> notyfyExpired();

}
