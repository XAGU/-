package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayArgsReqDTO;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayArgsRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author zcd
 * @date 18/4/17
 */
public interface IWxpayApi {

    /**
     * 获取支付宝app支付订单请求参数
     */
    @POST("wxpay/trade/app/pay/req/args")
    Observable<ApiResult<WxpayTradeAppPayArgsRespDTO>> requestWxpayArgs(@Body WxpayTradeAppPayArgsReqDTO reqDTO);
}
