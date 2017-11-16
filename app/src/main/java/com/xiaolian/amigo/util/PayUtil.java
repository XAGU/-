package com.xiaolian.amigo.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import com.xiaolian.amigo.util.Log;

import com.alipay.sdk.app.PayTask;
import com.xiaolian.amigo.data.enumeration.PayWay;
import com.xiaolian.amigo.ui.wallet.RechargeActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;


/**
 * Created by zcd on 2016/9/7.
 */

public class PayUtil {
    private static final String TAG = PayUtil.class.getSimpleName();
    /**
     * 成功
     */
    public static final int SUCCESS = 1;
    /**
     * 失败
     */
    public static final int DENIED = -1;
    /**
     * 取消
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

    // 微信支付
//    public static void wechatPay(IWXAPI iwxapi, WeChatPayapplyBean.DataBean dataBean) {
//        PayReq request = new PayReq();
//        request.appId = dataBean.getApp_id();
//        request.partnerId = dataBean.getMch_id();
//        request.prepayId = dataBean.getPrepay_id();
//        request.packageValue = dataBean.getPackageX();
//        request.nonceStr = dataBean.getNonce_str();
//        request.timeStamp = dataBean.getTime_stamp() + "";
//        request.sign = dataBean.getSign();
//        iwxapi.sendReq(request);
//    }

    /**
     * 支付宝支付
     *
     * @param activity activity
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
