package com.xiaolian.amigo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;

import java.io.ByteArrayOutputStream;

public class ShareUtils {
    private  static int mTargetScene = SendMessageToWX.Req.WXSceneSession;

    private static final int THUMB_SIZE = 150;
    public static void shareWX(String shareUrl ,String title ,String description , Context context ){

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareUrl ;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description =  description ;

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg ;
        req.transaction = buildTransaction("webpage");
        req.scene = mTargetScene ;
        MvpApp.mWxApi.sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }



    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
