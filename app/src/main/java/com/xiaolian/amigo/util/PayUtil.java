package com.xiaolian.amigo.util;

import android.app.Activity;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import com.alipay.sdk.app.PayTask;
import com.xiaolian.amigo.data.enumeration.PayWay;
import com.xiaolian.amigo.ui.wallet.RechargeActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import lombok.Data;


/**
 * 支付相关工具类
 *
 * @author zcd
 * @date 17/10/25
 */

public class PayUtil {
    private static final String TAG = PayUtil.class.getSimpleName();
    /**
     * 微信支付 成功
     */
    public static final int SUCCESS = 0;
    /**
     * 微信支付 失败
     * 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
     */
    public static final int DENIED = -1;
    /**
     * 微信支付 取消
     * 无需处理。发生场景：用户不支付了，点击取消，返回APP。
     */
    public static final int CANCEL = -2;
    /**
     * 支付宝
     */
    public static final int ALPAY = 100;
    /**
     * 微信支付
     */
    public static final int WECHAT_PAY = 100;

    /**
     * 微信支付
     *
     * @param iwxapi 微信api
     * @param req    微信请求数据
     */
    public static void weChatPay(IWXAPI iwxapi, IWeChatPayReq req) {
        PayReq request = new PayReq();
        request.appId = req.getAppId();
        request.partnerId = req.getPartnerId();
        request.prepayId = req.getPrepayId();
        request.packageValue = req.getPackageValue();
        request.nonceStr = req.getNonceStr();
        request.timeStamp = req.getTimeStamp();
        request.sign = req.getSign();
        iwxapi.sendReq(request);
    }

    /**
     * 微信请求数据接口
     */
    public interface IWeChatPayReq {
        String getAppId();

        String getPartnerId();

        String getPrepayId();

        String getPackageValue();

        String getNonceStr();

        String getTimeStamp();

        String getSign();
    }

    /**
     * 支付宝支付
     *
     * @param activity  activity
     * @param orderInfo 签名信息
     */
    public static void alpay(final Activity activity, final String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
//                String orderInfo = getSing();
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.d(TAG, result.toString());
                EventBus.getDefault().post(new RechargeActivity.PayEvent(PayWay.ALIAPY,
                        result));
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


}
