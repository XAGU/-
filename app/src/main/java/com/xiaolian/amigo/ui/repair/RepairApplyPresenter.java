/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.xiaolian.amigo.ui.repair;


import android.content.Context;
import android.net.Uri;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.xiaolian.amigo.data.base.OssClientHolder;
import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.data.manager.intf.IOssDataManager;
import com.xiaolian.amigo.data.manager.intf.IRepairDataManager;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.RepairApplyReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RepairProblemReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairApplyRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairProblemRespDTO;
import com.xiaolian.amigo.data.network.model.file.OssModel;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyView;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import retrofit2.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RepairApplyPresenter<V extends IRepairApplyView> extends BasePresenter<V>
        implements IRepairApplyPresenter<V> {

    private static final String TAG = RepairApplyPresenter.class.getSimpleName();
    private IRepairDataManager repairManager;
    private IUserDataManager userDataManager;
    private IOssDataManager ossDataManager;

    // oss token 失效信号量
    private final byte[] ossLock = new byte[0];
    private OssModel ossModel;
    private Random random = new Random();
    private int currentImagePosition;
    private ISharedPreferencesHelp sharedPreferencesHelp;
    private int currentPosition;

    @Inject
    public RepairApplyPresenter(IRepairDataManager repairmanager,
                                IUserDataManager userManager,
                                IOssDataManager ossDataManager,
                                ISharedPreferencesHelp sharedPreferencesHelp) {
        super();
        this.repairManager = repairmanager;
        this.userDataManager = userManager;
        this.ossDataManager = ossDataManager;
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }


    @Override
    public void onSubmit(List<Long> problems, List<String> images, String content, String mobile, int deviceType, Long locationId) {
        RepairApplyReqDTO reqDTO = new RepairApplyReqDTO();
        reqDTO.setCauseIds(problems);
        reqDTO.setDescription(content);
        reqDTO.setDeviceType(deviceType);
        reqDTO.setImages(images);
        reqDTO.setMobile(Long.valueOf(mobile));
        reqDTO.setResidenceId(locationId);

        addObserver(repairManager.applyRepair(reqDTO), new NetworkObserver<ApiResult<RepairApplyRespDTO>>() {

            @Override
            public void onReady(ApiResult<RepairApplyRespDTO> result) {
                if (null == result.getError()) {
                    Log.i(TAG, "报修成功");
                    getMvpView().onSuccess("报修成功");
                    getMvpView().backToRepairNav();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void onUpload(Context context, Uri imageUri, int position) {
        currentPosition = position;
        updateImage(context, imageUri.getPath());
    }

    @Override
    public void requestRepairProblems(int deviceType) {
        RepairProblemReqDTO reqDTO = new RepairProblemReqDTO();
        reqDTO.setPage(Constant.PAGE_START_NUM);
        reqDTO.setSize(Constant.PAGE_SIZE);
        reqDTO.setDeviceType(deviceType);
        addObserver(repairManager.queryRepairProblems(reqDTO), new NetworkObserver<ApiResult<RepairProblemRespDTO>>() {

            @Override
            public void onReady(ApiResult<RepairProblemRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().refreshProblems(result.getData().getCauses());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void setLastRepairTime(long l) {
        sharedPreferencesHelp.setLastRepairTime(l);
    }

    @Override
    public String getMobile() {
        return sharedPreferencesHelp.getUserInfo().getMobile();
    }

    private void updateImage(Context context, String filePath) {
        Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext(integer -> {
                    if (OssClientHolder.get() == null) {
                        initOssModel(context);
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // ignore
                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (OssClientHolder.get().getClient() == null) {
                            return;
                        }
                        getMvpView().post(() -> getMvpView().showLoading());
                        PutObjectRequest put = new PutObjectRequest(OssClientHolder.get().getOssModel().getBucket(),
                                generateObjectKey(String.valueOf(System.currentTimeMillis())),
                                filePath);
                        OSSAsyncTask task = OssClientHolder.get().getClient().asyncPutObject(put,
                                new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                                    @Override
                                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                        getMvpView().post(() -> getMvpView().hideLoading());
                                        getMvpView().post(() ->
                                                getMvpView().addImage(request.getObjectKey(), currentPosition));
                                        Log.d("PutObject", "UploadSuccess " + request.getObjectKey());
                                    }

                                    @Override
                                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                        getMvpView().post(() -> getMvpView().hideLoading());
                                        // Request exception
                                        if (clientExcepion != null) {
                                            // Local exception, such as a network exception
                                            clientExcepion.printStackTrace();
                                        }
                                        if (serviceException != null) {
                                            // Service exception
                                            Log.e("ErrorCode", serviceException.getErrorCode());
                                            Log.e("RequestId", serviceException.getRequestId());
                                            Log.e("HostId", serviceException.getHostId());
                                            Log.e("RawMessage", serviceException.getRawMessage());
                                        }
                                        // 失败后重新初始化ossClient
                                        initOssModel(context);
                                        getMvpView().post(() ->
                                                getMvpView().onError("图片上传失败，请重试"));
                                    }
                                });
                    }
                });
    }



    private void updateOssModel() {
        addObserver(ossDataManager.getOssModel(), new NetworkObserver<ApiResult<OssModel>>(false, true) {

            @Override
            public void onReady(ApiResult<OssModel> result) {
                if (null == result.getError()) {
                    OssClientHolder.get().setOssModel(result.getData());
                } else {
                    getMvpView().post(() -> getMvpView().onError(result.getError().getDisplayMessage()));
                }
                notifyOssResult();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                notifyOssResult();
                // ignore IllegalStateException
                if (e instanceof IllegalStateException) {
                    return;
                }
                if (e instanceof HttpException) {
                    if (((HttpException) e).code() == 600) {
                        return;
                    }
                }
                getMvpView().post(() -> getMvpView().onError("上传图片失败"));
            }
        }, Schedulers.io());
    }

    private void initOssModel(Context context) {
        addObserver(ossDataManager.getOssModel(), new NetworkObserver<ApiResult<OssModel>>(false, true) {

            @Override
            public void onReady(ApiResult<OssModel> result) {
                if (null == result.getError()) {
                    ossModel = result.getData();
                    OssClientHolder.get(context, ossModel, () -> {
                        Log.d(TAG, "oss token失效，获取token中...");
                        updateOssModel();
                        waitOssResult();
                        ossModel = OssClientHolder.get().getOssModel();
                        return new OSSFederationToken(ossModel.getAccessKeyId(),
                                ossModel.getAccessKeySecret(),
                                ossModel.getSecurityToken(),
                                (ossModel.getExpiration() - 2 * 60 * 1000)/1000);
                    });
                    notifyOssResult();
                } else {
                    notifyOssResult();
                    getMvpView().post(() -> getMvpView().onError(result.getError().getDisplayMessage()));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                notifyOssResult();
                // ignore IllegalStateException
                if (e instanceof IllegalStateException) {
                    return;
                }
                getMvpView().post(() -> getMvpView().onError("上传图片失败"));
            }
        }, Schedulers.io());
        waitOssResult();
    }

    private void notifyOssResult() {
        synchronized (ossLock) {
            ossLock.notifyAll();
        }
    }

    // 等待握手指令到达
    private void waitOssResult() {
        if (null == ossModel) {
            synchronized (ossLock) {
                if (null == ossModel) {
                    try {
                        Log.i(TAG, "等待oss到达");
                        ossLock.wait();
                        Log.i(TAG, "oss成功到达");
                    } catch (InterruptedException e) {
                        Log.wtf(TAG, e);
                    }
                }
            }
        }
    }

    private String generateObjectKey(String serverTime) {
        return OssFileType.REPAIR.getDesc() + "/" + userDataManager.getUser().getId() + "_"
                + serverTime + "_" + generateRandom() + ".jpg";
    }

    private String generateRandom() {
        int max = 9999;
        int min = 0;
        int num = random.nextInt(max) % (max - min + 1) + min;
        return String.format(Locale.getDefault(), "%04d", num);
    }
}
