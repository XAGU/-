package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingStatusReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderCurrentRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRoomReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRouteRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BookingQueueProgressDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BuildingTrafficDTO;
import com.xiaolian.amigo.data.network.model.bathroom.CurrentBathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.TryBookingResultRespDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 公共浴室
 *
 * @author zcd
 * @date 18/7/4
 */
public interface IBathroomApi {
    /**
     * 获取当前楼栋的浴室房间信息
     */
    @POST("bath/room/tree")
    Observable<ApiResult<BathBuildingRespDTO>> tree(@Body SimpleReqDTO reqDTO);

    /**
     * 预约设备
     */
    @POST("bath/trade/booking")
    Observable<ApiResult<TryBookingResultRespDTO>> booking(@Body BathBookingReqDTO reqDTO);


    /**
     * 用户取消预约或者购买的编码
     */
    @POST("bath/trade/booking/cancel")
    Observable<ApiResult<BooleanRespDTO>> cancel(@Body SimpleReqDTO reqDTO);

    /**
     * 检查当前用户是否有已经预约或者购买了编码的
     */
    @POST("bath/trade/check")
    Observable<ApiResult<BathOrderRespDTO>> check(@Body BathBookingReqDTO reqDTO);

    /**
     * 查询指定预约订单状态
     */
    @POST("bath/trade/booking/query")
    Observable<ApiResult<BathBookingRespDTO>> query(@Body BathBookingStatusReqDTO reqDTO);


    /**
     * 查询指定订单状态
     */
    @POST("bath/trade/order/query")
    Observable<ApiResult<BathOrderCurrentRespDTO>> orderQuery(@Body SimpleReqDTO reqDTO);


    /**
     * 根据当前登录用户所在学校配置，以及用户上次洗澡的习惯，决定路由到"宿舍热水澡模块"、还是"公共浴室模块"
     */
    @POST("bath/room/route")
    Observable<ApiResult<BathRouteRespDTO>> route();

    /**
     * 该方法用于锁定指定设备,以及返回预付信息和红包信息，用户余额，设备详细位置，失约次数，支付过期时间等
     */
    @POST("bath/trade/preBooking")
    Observable<ApiResult<BathPreBookingRespDTO>> preBooking(@Body BathBookingReqDTO reqDTO);

    /**
     * 该方法用于购买洗澡劵前的操作，返回预付信息和红包信息，用户余额，设备详细位置等信息
     */
    @POST("bath/trade/preBuyVoucher")
    Observable<ApiResult<BathPreBookingRespDTO>> preBuyVoucher();

    /**
     * 校验验证码
     */
    @POST("bath/bathPassword/update/check")
    Observable<ApiResult<BooleanRespDTO>> checkVerifyCode(@Body VerificationCodeCheckReqDTO reqDTO);

    /**
     * 更新浴室密码
     * @param reqDTO
     * @return
     */
    @POST("bath/bathPassword/update")
    Observable<ApiResult<SimpleRespDTO>> updateBathroomPassword(@Body BathPasswordUpdateReqDTO reqDTO);


    /**
     * 获取订单记录
     * @param reqDTO
     * @return
     */
    @POST("bath/order/list")
    Observable<ApiResult<QueryBathOrderListRespDTO>> getOrderRecordList(@Body QueryBathOrderListReqDTO reqDTO);


    /**
     * 预约前操作，获取是否存在之前订单
     * @return
     */
    @POST("bath/precondition")
    Observable<ApiResult<BathOrderPreconditionRespDTO>> getOrderPrecondition() ;

    /**
     * 解除设备绑定
     */
    @POST("bath/trade/unlock")
    Observable<ApiResult<BooleanRespDTO>> unlock(@Body BathRoomReqDTO reqDTO);


    /**
     * 主页上显示是否有上一次订单
     * @return
     */
    @POST("bath/order/current")
    Observable<ApiResult<CurrentBathOrderRespDTO>> currentOrder();

    /**
     * 客户端请求结账
     */
    @POST("bath/trade/askSettle")
    Observable<ApiResult<BooleanRespDTO>> askSettle(@Body  SimpleReqDTO reqDTO);


    /**
     * 查询每个楼层的使用流量
     */
    @POST("bath/trade/building/traffic")
    Observable<ApiResult<BuildingTrafficDTO>>  traffi(@Body SimpleReqDTO reqDTO) ;


    /**
     * 查询指定排队订单的信息
     * @param reqDTO
     * @return
     */
    @POST("bath/trade/queue/progress")
    Observable<ApiResult<BookingQueueProgressDTO>> queueQuery(@Body SimpleReqDTO reqDTO);


    /**
     * 取消预约排队
     * @param reqDTO
     * @return
     */
    @POST("bath/trade/queue/cancel")
    Observable<ApiResult<BooleanRespDTO>> cancelQueue(@Body SimpleReqDTO reqDTO);


    /**
     * 提醒服务器超时
     * @return
     */
    @POST("bath/trade/booking/notify/expired")
    Observable<ApiResult<BooleanRespDTO>> notyfyExpired();
}
