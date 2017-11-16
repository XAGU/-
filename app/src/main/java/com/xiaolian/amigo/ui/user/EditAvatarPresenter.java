package com.xiaolian.amigo.ui.user;

import android.content.Context;
import android.net.Uri;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.base.OssClientHolder;
import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.data.manager.intf.IOssDataManager;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryAvatarDTO;
import com.xiaolian.amigo.data.network.model.file.OssModel;
import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.adaptor.EditAvatarAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditAvatarPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditAvatarVIew;
import com.xiaolian.amigo.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <p>
 * Created by zcd on 9/27/17.
 */

public class EditAvatarPresenter<V extends IEditAvatarVIew> extends BasePresenter<V>
        implements IEditAvatarPresenter<V> {

    private static final String TAG = EditAvatarPresenter.class.getSimpleName();
    private IUserDataManager userDataManager;
    private IOssDataManager ossDataManager;
    // oss token 失效信号量
    private final byte[] ossLock = new byte[0];
    private OssModel ossModel;
    private Random random = new Random();

    @Inject
    public EditAvatarPresenter(IUserDataManager userDataManager, IOssDataManager ossDataManager) {
        super();
        this.ossDataManager = ossDataManager;
        this.userDataManager = userDataManager;
        //if null , default will be init
    }


    @Override
    public void getAvatarList() {
        addObserver(userDataManager.getAvatarList(), new NetworkObserver<ApiResult<QueryAvatarDTO>>() {

            @Override
            public void onReady(ApiResult<QueryAvatarDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getAvatars() != null && result.getData().getAvatars().size() > 0) {
                        List<EditAvatarAdaptor.AvatarWrapper> wrappers = new ArrayList<>();
                        for (String url : result.getData().getAvatars()) {
                            wrappers.add(new EditAvatarAdaptor.AvatarWrapper(url));
                        }
                        getMvpView().addAvatar(wrappers);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

        });
    }

    @Override
    public void uploadImage(Context context, Uri imageUri) {
        updateImage(context, imageUri.getPath());
    }

    @Override
    public void updateAvatarUrl(String avatarUrl) {
        PersonalUpdateReqDTO dto = new PersonalUpdateReqDTO();
        dto.setPictureUrl(avatarUrl);
        addObserver(userDataManager.updateUserInfo(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {
            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess(R.string.change_avatar_success);
                    userDataManager.setUser(new User(result.getData()));
                    getMvpView().finishView();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
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
                        PutObjectRequest put = new PutObjectRequest(OssClientHolder.get().getOssModel().getBucket(),
                                generateObjectKey(String.valueOf(System.currentTimeMillis())),
                                filePath);
                        OSSAsyncTask task = OssClientHolder.get().getClient().asyncPutObject(put,
                                new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                                    @Override
                                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                        getMvpView().post(() -> getMvpView().setAvatar(request.getObjectKey()));
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
        return OssFileType.AVATAR.getDesc() + "/" + userDataManager.getUser().getId() + "_"
                + serverTime + "_" + generateRandom()  + ".jpg";
    }

    private String generateRandom() {
        int max = 9999;
        int min = 0;
        int num = random.nextInt(max) % (max - min + 1) + min;
        return String.format(Locale.getDefault(), "%04d", num);
    }
}
