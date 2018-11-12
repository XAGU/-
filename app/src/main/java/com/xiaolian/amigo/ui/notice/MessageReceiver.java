package com.xiaolian.amigo.ui.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.xiaolian.amigo.BuildConfig;
import com.xiaolian.amigo.data.base.LogInterceptor;
import com.xiaolian.amigo.data.network.IDeviceConnectErrorApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.connecterror.DeviceConnectErrorReqDTO;
import com.xiaolian.amigo.data.network.model.notify.CustomDTO;
import com.xiaolian.amigo.data.prefs.SharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.repair.RepairDetailActivity;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author zcd
 * @date 18/6/2
 */
public class MessageReceiver extends XGPushBaseReceiver {
    private static final String TAG = MessageReceiver.class.getSimpleName();
    private static final String DeviceLogFileName = "DeviceLog.txt";
    private Gson mGson = new Gson();
    private Context context;
    private SharedPreferencesHelp sharedPreferencesHelp;
    public CompositeSubscription subscriptions;

    //    private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
    @Override
    public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult message) {
        if (context == null || message == null) {
            return;
        }
        subscriptions = new CompositeSubscription();
        this.context = context;
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = message + "注册成功";
            // 在这里拿token
            String token = message.getToken();
        } else {
            text = message + "注册失败错误码：" + errorCode;
        }
        Log.d(TAG, text);


    }


    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功";
        } else {
            text = "反注册失败" + errorCode;
        }
        Log.d(TAG, text);
    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
        Log.d(TAG, text);
    }

    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        Log.d(TAG, text);
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        String text = "收到消息:" + message.toString();
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        Log.wtf(TAG, text);
//        if (customContent != null && customContent.length() != 0) {
//            try {
//                JSONObject obj = new JSONObject(customContent);
//                // key1为前台配置的key
//                if (!obj.isNull("key")) {
//                    String value = obj.getString("key");
//                    Log.d(TAG, "get custom value:" + value);
//                }
//                // ...
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        this.context = context ;
        Gson gson = new Gson();
        sharedPreferencesHelp = new SharedPreferencesHelp(context, gson);
        CustomDTO customDTO = mGson.fromJson(customContent, CustomDTO.class);
        Log.d(TAG, "++++++++++++++++透传消息");
        // APP自主处理消息的过程...
        if (customDTO == null){
            uploadLog();
            return ;
        }
        Log.d(TAG, text);
        switch (customDTO.getAction()) {
            case 1:
                if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(CommonUtil.getRuningApp(context.getApplicationContext()))) {
                    CommonUtil.sendNotify(context.getApplicationContext(), (int) (Math.random() * 10000), message.getTitle(),
                            message.getTitle(), message.getContent(), null, null);
                } else {
                    CommonUtil.sendNotify(context.getApplicationContext(), (int) (Math.random() * 10000), message.getTitle(),
                            message.getTitle(), message.getContent(), MainActivity.class, null);
                }
                break;
            case 2:
                Bundle bundle = new Bundle();
                bundle.putLong(Constant.BUNDLE_ID, customDTO.getTargetId());
                CommonUtil.sendNotify(context.getApplicationContext(), (int) (Math.random() * 10000), message.getTitle(),
                        message.getTitle(), message.getContent(), RepairDetailActivity.class, bundle);
                break;

            // 排队预约成功通知
//            case 7:
//                Bundle queueBundle = new Bundle();
//                queueBundle.putBoolean(Constant.BUNDLE_ID, true);
//                CommonUtil.sendNotify(context.getApplicationContext() ,(int) (Math.random() * 10000) ,message.getTitle() ,
//                        message.getTitle(), message.getContent(), MainActivity.class  , queueBundle);
//                break ;


        }
    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击。此处不能做点击消息跳转，详细方法请参照官网的Android常见问题文档
    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult message) {
        Log.e("LC", "+++++++++++++++ 通知被点击 跳转到指定页面。");
//        NotificationManager notificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancelAll();
        if (context == null || message == null) {
            return;
        }
        this.context = context ;
        String text = "";
        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            text = "通知被打开 :" + message;
            String customContent = message.getCustomContent();
            CustomDTO customDTO = mGson.fromJson(customContent, CustomDTO.class);
            switch (customDTO.getAction()) {
                case 7:
                    Bundle queueBundle = new Bundle();
                    queueBundle.putBoolean(Constant.BUNDLE_ID, true);
                    context.startActivity(new Intent(context, MainActivity.class)
                            .putExtra(Constant.BUNDLE_ID, true));
                    break;
            }

        } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
            text = "通知被清除 :" + message;
        }
//        Toast.makeText(context, "广播接收到通知被点击:" + message.toString(),
//                Toast.LENGTH_SHORT).show();
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        Log.d(TAG, "custom: " + message.getCustomContent());
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    String value = obj.getString("key");
                    Log.d(TAG, "get custom value:" + value);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // APP自主处理的过程。。。
        Log.d(TAG, text);

    }

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult notifiShowedRlt) {
        if (context == null || notifiShowedRlt == null) {
            return;
        }
//        context.sendBroadcast(intent);
//        show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());
        Log.d("LC", "+++++++++++++++++++++++++++++展示通知的回调");
    }


    /**
     * 上传log 日志
     */
    protected void uploadLog() {
        String model = Build.MODEL;
        String brand = Build.BRAND;
        String appVersion = AppUtils.getVersionName(context);
        int systemVersion = Build.VERSION.SDK_INT;
        String mobile = sharedPreferencesHelp.getUserInfo().getMobile();
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaolian/" + sharedPreferencesHelp.getUserInfo().getId() + "/";
        File path = new File(filePath);
        if (!path.exists() && !path.mkdirs()) {
            return;
        }

        File logFile = new File(filePath, DeviceLogFileName);
        try {
            uploadErrorLog(model, brand, systemVersion + "", appVersion, mobile, "", logFile);
        }catch (Exception e){
          Log.wtf(TAG ,e.getMessage());
        }
    }


    /**
     * 上传日志
     *
     * @param brand
     * @param version
     * @param appVersion
     * @param mobile
     * @param orderNo
     * @param logFile
     */
    protected void uploadErrorLog(String model, String brand, String version, String appVersion, String mobile, String orderNo,
                                  File logFile) throws Exception {
        if (logFile == null || !logFile.exists()) return;
        RequestBody requestBody;
        MultipartBody.Builder builder;

        builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("model", model)
                .addFormDataPart("brand", brand)
                .addFormDataPart("version", version)
                .addFormDataPart("appVersion", appVersion)
                .addFormDataPart("mobile", mobile)
                .addFormDataPart("orderNo", orderNo)
                .addFormDataPart("uploadType", 1 + "")
                .addFormDataPart("system" ,2+"")
                .addFormDataPart("logContent", logFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), logFile));
        requestBody = builder.build();

        LogInterceptor logInterceptor = new LogInterceptor(sharedPreferencesHelp);

        Retrofit retrofit = provideRetrofit(logInterceptor);

        IDeviceConnectErrorApi connectErrorApi = retrofit.create(IDeviceConnectErrorApi.class);
        connectErrorApi.uploadLog(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResult<BooleanRespDTO>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG ,e.getMessage());
                    }

                    @Override
                    public void onNext(ApiResult<BooleanRespDTO> result) {
                        if (result.getError() == null) {
                            Log.wtf(TAG, result.getData().getFailReason() + "    " + result.getData().isResult());
                        }
                    }
                });
//                .subscribe(result -> {
//                    if (result.getError() == null){
//                        Log.wtf(TAG ,result.getData().getFailReason() +"    " +result.getData().isResult());
//                    }
//                });




    }


    Retrofit provideRetrofit(LogInterceptor logInterceptor) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(logInterceptor)
                .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .build();
        return new Retrofit.Builder()
                .baseUrl(Constant.SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

}
