package com.xiaolian.amigo.ui.more;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.xiaolian.amigo.data.base.LogInterceptor;
import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.more.intf.IAboutUsPresenter;
import com.xiaolian.amigo.ui.more.intf.IAboutUsView;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 关于我们presenter
 *
 * @author zcd
 * @date 17/11/9
 */

public class AboutUsPresenter<V extends IAboutUsView> extends BasePresenter<V>
        implements IAboutUsPresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = AboutUsPresenter.class.getSimpleName();
    private IMainDataManager mainDataManager;
    private LogInterceptor interceptor;

    private static final String DeviceLogFileName = "DeviceLog.txt";

    @Inject
    AboutUsPresenter(IMainDataManager manager, LogInterceptor interceptor) {
        this.mainDataManager = manager;
        this.interceptor = interceptor;
    }

    @Override
    public void checkUpdate(Integer code, String versionNo, boolean click) {
        CheckVersionUpdateReqDTO reqDTO = new CheckVersionUpdateReqDTO();
        reqDTO.setVersionNo(versionNo);
        addObserver(mainDataManager.checkUpdate(reqDTO),
                new NetworkObserver<ApiResult<CheckVersionUpdateRespDTO>>(false) {

                    @Override
                    public void onReady(ApiResult<CheckVersionUpdateRespDTO> result) {
                        if (null == result.getError()) {
                            if (result.getData().getResult()) {
                                // 是否是用户点击
                                if (click) {
                                    getMvpView().showUpdateDialog(result.getData().getVersion());
                                } else {
                                    getMvpView().showUpdateButton(result.getData().getVersion());
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void changeHost(String server, String h5Server, String bathroomServer) {
        interceptor.setServer(server);
        interceptor.setH5Server(h5Server);
        interceptor.setBathServer(bathroomServer);
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
            getMvpView().rlClickAble();
            return;
        }

        File logFile = new File(filePath, DeviceLogFileName);
        try {
            uploadErrorLog(model, brand, systemVersion + "", appVersion, mobile, "", logFile);
        }catch (Exception e){
            getMvpView().rlClickAble();
            Log.wtf(TAG ,e.getMessage());
        }
    }

    @Override
    public Long getUserId() {
        return mainDataManager.getUserInfo().getId();
    }

    @Override
    public String getAccessToken() {
        return mainDataManager.getAccessToken();
    }

    @Override
    public String getRefreshToken() {
        return mainDataManager.getRefreshToken();
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
                                  File logFile) {
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
                .addFormDataPart("system", 2 + "")
                .addFormDataPart("logContent", logFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), logFile));
        requestBody = builder.build();
        addObserver(mainDataManager.uploadLog(requestBody), new NetworkObserver<ApiResult<BooleanRespDTO>>() {


            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                getMvpView().rlClickAble();
                if (result.getError() == null) {
                    Log.wtf(TAG, result.getData().getFailReason() + "    " + result.getData().isResult());
                    getMvpView().uploadSuccess();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().rlClickAble();
            }
        });
    }
}
