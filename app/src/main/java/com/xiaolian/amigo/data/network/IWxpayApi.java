package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayArgsReqDTO;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayArgsRespDTO;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayResultParseReqDTO;
import com.xiaolian.amigo.data.network.model.wxpay.WxpayTradeAppPayResultParseRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author zcd
 * @date 18/4/17
 */
public interface IWxpayApi {

    /**
     * 获取app支付订单请求参数
     */
    @POST("wxpay/trade/app/pay/req/args")
    Observable<ApiResult<WxpayTradeAppPayArgsRespDTO>> requestWxpayArgs(@Body WxpayTradeAppPayArgsReqDTO reqDTO);

    /**
     * 解析app支付结果
     */
    @POST("wxpay/trade/app/pay/resp/result/parse")
    Observable<ApiResult<WxpayTradeAppPayResultParseRespDTO>> parseWxpayResule(@Body WxpayTradeAppPayResultParseReqDTO reqDTO);
}
