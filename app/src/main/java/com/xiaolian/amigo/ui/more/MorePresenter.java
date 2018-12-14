package com.xiaolian.amigo.ui.more;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.manager.intf.IMoreDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.more.intf.IMorePresenter;
import com.xiaolian.amigo.ui.more.intf.IMoreView;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 更多
 *
 * @author zcd
 * @date 17/10/13
 */

public class MorePresenter<V extends IMoreView> extends BasePresenter<V>
        implements IMorePresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = MorePresenter.class.getSimpleName();
    private IMoreDataManager moreDataManager;

    private IMainDataManager mainDataManager ;
    private static final String DeviceLogFileName = "DeviceLog.txt";

    @Inject
    MorePresenter(IMoreDataManager moreDataManager , IMainDataManager mainDataManager) {
        super();
        this.moreDataManager = moreDataManager;
        this.mainDataManager = mainDataManager ;
    }

    @Override
    public void onAttach(V view) {
        super.onAttach(view);
        if (moreDataManager.getTransfer()) {
            getMvpView().showTransfer();
        }
    }

    @Override
    public void logout() {

        addObserver(mainDataManager.revokeToken(), new NetworkObserver<ApiResult<Void>>() {

            @Override
            public void onReady(ApiResult<Void> voidApiResult) {
                moreDataManager.logout();
                getMvpView().onSuccess("退出登录成功");
                getMvpView().redirectToLogin();
            }
            
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                moreDataManager.logout();
                getMvpView().onSuccess("退出登录成功");
                getMvpView().redirectToLogin();
            }
        });


    }

    @Override
    public String getAccessToken() {
        return moreDataManager.getAccessToken();
    }

    @Override
    public String getRefreshToken() {
        return moreDataManager.getRefreshToken();
    }


    @Override
    public Long getUserId() {
        return moreDataManager.getUserId();
    }

    @Override
    public void deletePushToken() {
        moreDataManager.deletePushToken();
    }

    @Override
    public Long getSchoolId() {
        return moreDataManager.getSchoolId();
    }

    @Override
    public String getPushTag() {
        return moreDataManager.getPushTag();
    }

    @Override
    public void setPushTag(String pushTag) {
        moreDataManager.setPushTag(pushTag);
    }

    @Override
    public void uploadErrorLog() {
        String model = Build.MODEL;
        String brand = Build.BRAND;
        String appVersion = getMvpView().getVersionName();
        int systemVersion = Build.VERSION.SDK_INT;
        String mobile = mainDataManager.getUserInfo().getMobile();
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaolian/" + mainDataManager.getUserInfo().getId() + "/";
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
        addObserver(moreDataManager.uploadLog(requestBody) ,new NetworkObserver<ApiResult<BooleanRespDTO>>(){


            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (result.getError() == null) {
                    Log.wtf(TAG, result.getData().getFailReason() + "    " + result.getData().isResult());
                    getMvpView().onSuccess("错误上报成功");
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });




    }
}
