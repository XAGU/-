package com.xiaolian.amigo.util;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaolian.amigo.MvpApp;

public class ShareUtils {
    private  static int mTargetScene = SendMessageToWX.Req.WXSceneSession;

    private static IWXAPI api;
    public static void shareWX(String shareUrl , String appId , Context context){

        WXTextObject textObject = new WXTextObject();
        textObject.text = shareUrl ;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject ;
        msg.description = shareUrl ;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "text" ;
        req.message = msg ;
        req.transaction = buildTransaction("text");
        req.scene = mTargetScene ;

        if (!TextUtils.isEmpty(appId)){
            api = WXAPIFactory.createWXAPI(context, Constant.WECHAT_APP_ID);
            api.sendReq(req);
        }else{
            MvpApp.mWxApi.sendReq(req);
        }
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
