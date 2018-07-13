package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;

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
    @POST("bathRoom/list")
    Observable<ApiResult<BathBuildingRespDTO>> list(@Body SimpleReqDTO reqDTO);

    /**
     * 预约设备
     */
    @POST("bath/trade/booking")
    Observable<ApiResult<BathBookingRespDTO>> booking(@Body SimpleReqDTO reqDTO);

    /**
     * 用户支付预约或者购买编码
     */
    @POST("bath/trade/pay")
    Observable<ApiResult<BathBookingRespDTO>> pay(@Body SimpleReqDTO reqDTO);

    /**
     * 用户取消预约或者购买的编码
     */
    @POST("bath/trade/cancel")
    Observable<ApiResult<SimpleRespDTO>> cancel(@Body BathBookingReqDTO reqDTO);

    /**
     * 检查当前用户是否有已经预约或者购买了编码的
     */
    @POST("bath/trade/check")
    Observable<ApiResult<BathOrderRespDTO>> check(@Body BathBookingReqDTO reqDTO);

    /**
     * 查询指定订单状态
     */
    @POST("bath/trade/query")
    Observable<ApiResult<BathOrderRespDTO>> query(@Body BathBookingReqDTO reqDTO);

}
