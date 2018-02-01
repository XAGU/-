package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayArgsReqDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayResultParseReqDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayArgsRespDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayTradeAppPayResultParseRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * alipay api
 *
 * @author zcd
 * @date 17/12/14
 */

public interface IAlipayApi {
    /**
     * 获取支付宝app支付订单请求参数
     */
    @POST("alipay/trade/app/pay/req/args")
    Observable<ApiResult<AlipayTradeAppPayArgsRespDTO>> requestAlipayArgs(@Body AlipayTradeAppPayArgsReqDTO reqDTO);

    /**
     * 解析app支付结果
     */
    @POST("alipay/trade/app/pay/resp/result/parse")
    Observable<ApiResult<AlipayTradeAppPayResultParseRespDTO>> parseAlipayResule(@Body AlipayTradeAppPayResultParseReqDTO reqDTO);
}
