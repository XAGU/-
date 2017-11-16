package com.xiaolian.amigo.ui.lostandfound;

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
import com.xiaolian.amigo.data.enumeration.LostAndFound;
import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.manager.intf.IOssDataManager;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.SaveLostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.file.OssModel;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostView;
import com.xiaolian.amigo.util.Log;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 失物发布
 * <p>
 * Created by zcd on 9/21/17.
 */

public class PublishLostPresenter<V extends IPublishLostView> extends BasePresenter<V>
        implements IPublishLostPresenter<V> {
    private static final String TAG = PublishLostPresenter.class.getSimpleName();
    private ILostAndFoundDataManager lostAndFoundManager;
    private IUserDataManager userDataManager;

    private IOssDataManager ossDataManager;
    // oss token 失效信号量
    private final byte[] ossLock = new byte[0];
    private OssModel ossModel;
    private Random random = new Random();
    private int currentImagePosition;
    private OssFileType currentType;

    @Inject
    PublishLostPresenter(ILostAndFoundDataManager lostAndFoundManager,
                         IUserDataManager userManager,
                         IOssDataManager ossDataManager) {
        super();
        this.lostAndFoundManager = lostAndFoundManager;
        this.userDataManager = userManager;
        this.ossDataManager = ossDataManager;
    }

    @Override
    public void publishLostAndFound(String desc, List<String> images, String itemName,
                                    String location, Long lostTime, String mobile, String title,
                                    Integer type) {
        SaveLostAndFoundDTO dto = new SaveLostAndFoundDTO();
        dto.setDescription(desc);
        dto.setImages(images);
        dto.setItemName(itemName);
        dto.setLocation(location);
        dto.setLostTime(lostTime);
        dto.setMobile(mobile);
        dto.setTitle(title);
        dto.setType(type);
        addObserver(lostAndFoundManager.saveLostAndFounds(dto), new NetworkObserver<ApiResult<SimpleRespDTO>>() {

            @Override
            public void onReady(ApiResult<SimpleRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess(LostAndFound.getLostAndFound(type).getDesc() + "发布成功");
                    getMvpView().finishView();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void uploadImage(Context context, Uri imageUri, int position, OssFileType type) {
        currentImagePosition = position;
        currentType = type;
        uploadImage(context, imageUri.getPath());
//        RequestBody image = RequestBody.create(MediaType.parse(Constant.UPLOAD_IMAGE_CONTENT_TYPE),
//                new File(imageUri.getPath()));
//        addObserver(userDataManager.uploadFile(image), new NetworkObserver<ApiResult<String>>() {
//
//            @Override
//            public void onReady(ApiResult<String> result) {
//                if (null == result.getError()) {
//                    getMvpView().addImage(result.getData(), position);
//                } else {
//                    getMvpView().onError(result.getError().getDisplayMessage());
//                }
//            }
//        });
    }

    private void uploadImage(Context context, String filePath) {
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
                        PutObjectRequest put = new PutObjectRequest(OssClientHolder.get().getOssModel().getBucket(),
                                generateObjectKey(String.valueOf(System.currentTimeMillis())),
                                filePath);
                        OSSAsyncTask task = OssClientHolder.get().getClient().asyncPutObject(put,
                                new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                                    @Override
                                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                        getMvpView().post(() -> getMvpView().addImage(request.getObjectKey(), currentImagePosition));
                                        Log.d("PutObject", "UploadSuccess " + request.getObjectKey());
                                    }

                                    @Override
                                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
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
                                ossModel.getExpiration());
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
        return currentType.getDesc() + "/" + userDataManager.getUser().getId() + "_"
                + serverTime + "_" + generateRandom() + ".jpg";
    }

    private String generateRandom() {
        int max = 9999;
        int min = 0;
        int num = random.nextInt(max) % (max - min + 1) + min;
        return String.format(Locale.getDefault(), "%04d", num);
    }
}
