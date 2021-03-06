package com.xiaolian.amigo.ui.user;

import android.content.Context;
import android.net.Uri;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.base.OssClientHolder;
import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.data.manager.intf.IOssDataManager;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.file.OssModel;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.QueryAvatarDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.adaptor.EditAvatarAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditAvatarPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditAvatarView;
import com.xiaolian.amigo.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import retrofit2.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 编辑头像
 *
 * @author zcd
 * @date 17/9/27
 */

public class EditAvatarPresenter<V extends IEditAvatarView> extends BasePresenter<V>
        implements IEditAvatarPresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = EditAvatarPresenter.class.getSimpleName();
    private IUserDataManager userDataManager;
    private Random random = new Random();

    @Inject
    EditAvatarPresenter(IUserDataManager userDataManager, IOssDataManager ossDataManager) {
        super();
        this.userDataManager = userDataManager;
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
        userDataManager.getOssModel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResult<OssModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onHttpError(e);
                    }

                    @Override
                    public void onNext(ApiResult<OssModel> result) {
                        Log.wtf(TAG ," keyId:   "+result.getData().getAccessKeyId()
                                +'\n' + "endpoint:  "  + result.getData().getEndpoint() +'\n'
                                +" accessKeySecret:   " + result.getData().getAccessKeySecret()
                        +'\n' + "  filePath :" + filePath);
                        uploadImage(OssClientHolder.getClient(context, result.getData()), result.getData(), filePath);
                    }
                });
    }

    private void uploadImage(OSSClient client, OssModel model, String filePath) {

        getMvpView().post(() -> getMvpView().showLoading());
        PutObjectRequest put = new PutObjectRequest(model.getBucket(),
                generateObjectKey(String.valueOf(System.currentTimeMillis())),
                filePath);

        Log.wtf(TAG ,put.getObjectKey());
        OSSAsyncTask task = client.asyncPutObject(put,
                new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        getMvpView().post(() -> getMvpView().hideLoading());
                        getMvpView().post(() -> getMvpView().setAvatar(request.getObjectKey()));
                        userDataManager.getUser().setPictureUrl(request.getObjectKey());
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
                        getMvpView().post(() ->
                                getMvpView().onError("图片上传失败，请重试"));
                    }
                });
    }

    private void onHttpError(Throwable e) {
        if (e instanceof HttpException) {
            switch (((HttpException) e).code()) {
                case 401:
                    getMvpView().post(() -> getMvpView().onError(R.string.please_login));
                    getMvpView().post(() -> getMvpView().redirectToLogin(false));
                    break;
                default:
                    getMvpView().post(() ->
                            getMvpView().onError("图片上传失败，请重试"));
                    break;
            }
        } else {
            getMvpView().post(() ->
                    getMvpView().onError("图片上传失败，请重试"));
        }
    }

    private String generateObjectKey(String serverTime) {
        return OssFileType.AVATAR.getDesc() + "/" + userDataManager.getUser().getId() + "_"
                + serverTime + "_" + generateRandom() + ".jpg";
    }

    private String generateRandom() {
        int max = 9999;
        int min = 0;
        int num = random.nextInt(max) % (max - min + 1) + min;
        return String.format(Locale.getDefault(), "%04d", num);
    }
}
