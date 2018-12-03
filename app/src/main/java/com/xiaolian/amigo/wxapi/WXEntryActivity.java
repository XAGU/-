package com.xiaolian.amigo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.ui.login.LoginActivity;
import com.xiaolian.amigo.ui.user.EditProfileActivity;
import com.xiaolian.amigo.util.Constant;

import org.greenrobot.eventbus.EventBus;

/**
 * 微信入口
 *
 * @author zcd
 * @date 18/2/2
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MvpApp.mWxApi.handleIntent(getIntent(),this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        MvpApp.mWxApi.handleIntent(intent,this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.e(TAG, "onResp: ERR_OK" );
                //即为所需的code
                String code = "";
                try {
                    //微信登录使用到的code
                    code = ((SendAuth.Resp) resp).code;
                } catch (Exception e) {
                    Log.e(TAG, "onResp: " + e.getMessage() );
                    finish();
                    return;

                }
                //如果code值不为空，则说明是微信登录
                if (!TextUtils.isEmpty(code)) {
                    EditProfileActivity.Event event = new EditProfileActivity.Event(EditProfileActivity.Event.EventType.WECHAT_CODE);
                    event.setMsg(code);
                    EventBus.getDefault().post(event);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.e(TAG, "onResp:ERR_USER_CANCEL ");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.e(TAG, "onResp:ERR_AUTH_DENIED " );
                break;
            default:
                break;
        }
        finish();
    }
}
