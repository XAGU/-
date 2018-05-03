package com.xiaolian.amigo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaolian.amigo.data.enumeration.PayWay;
import com.xiaolian.amigo.ui.wallet.RechargeActivity;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.PayUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 微信支付入口
 *
 * @author zcd
 * @date 18/2/2
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);

//        api = WXAPIFactory.createWXAPI(this, "wx95afa85f1dec8af6");
        api = WXAPIFactory.createWXAPI(this, null);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode
                + " type = " + resp.getType());

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case PayUtil.SUCCESS:
                    Log.d(TAG, "支付成功");
                    break;
                case PayUtil.DENIED:
                    Log.d(TAG, "支付拒绝");
                    break;
                case PayUtil.CANCEL:
                    Log.d(TAG, "支付取消");
                    break;
                default:
                    break;
            }
            EventBus.getDefault().post(new RechargeActivity.PayEvent(PayWay.WECHAT,
                    resp.errCode));
        }
        finish();
    }
}