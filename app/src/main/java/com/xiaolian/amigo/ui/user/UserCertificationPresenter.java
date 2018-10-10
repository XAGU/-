package com.xiaolian.amigo.ui.user;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

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
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationPresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationView;

import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import retrofit2.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserCertificationPresenter <v extends IUserCertificationView> extends BasePresenter<v>
    implements IUserCertificationPresenter<v>{

    private IUserDataManager userDataManager ;


    private  final String TAG = this.getClass().getSimpleName();
    private IOssDataManager ossDataManager;
    private Random random = new Random();
    private int currentImagePosition;
    private OssFileType currentType;
    private ImageView iv ;


    @Inject
    public UserCertificationPresenter(IUserDataManager userDataManager ,IOssDataManager ossDataManager){
        super();
        this.userDataManager = userDataManager ;
        this.ossDataManager = ossDataManager ;

    }

    @Override
    public User getUserInfo() {
        return userDataManager.getUser();
    }


    @Override
    public void uploadImage(Context activity, String imagePath, int position, OssFileType type) {
        Log.d(TAG ," " + imagePath);
        currentImagePosition = position;
        currentType = type;
        uploadImage(activity,imagePath);
    }

    @Override
    public void uploadImage(Context activity, String imagePath, OssFileType found, ImageView iv) {
        this.iv = iv ;
        this.currentType = found ;
        uploadImage(activity ,imagePath);
    }

    @Override
    public String getDormitory() {
        User user = userDataManager.getUser();
        if (user != null){
            return user.getDormitory();
        }else{
            return "";
        }
    }


    private void uploadImage(Context context, String filePath) {
        ossDataManager.getOssModel()
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

                        uploadImage(OssClientHolder.getClient(context, result.getData()), result.getData(), filePath);
                    }
                });
    }


    @SuppressWarnings("unused")
    private void uploadImage(OSSClient client, OssModel model, String filePath) {
        getMvpView().post(() -> getMvpView().showLoading());
        PutObjectRequest put = new PutObjectRequest(model.getBucket(),
                generateObjectKey(String.valueOf(System.currentTimeMillis())),
                filePath);
        OSSAsyncTask task = client.asyncPutObject(put,
                new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        getMvpView().post(() -> getMvpView().hideLoading());
                        if (iv  != null){
                            getMvpView().post(() -> {getMvpView().setCardImage(iv ,filePath , request.getObjectKey());
                                iv = null ;

                            });
                        }else {
                            getMvpView().post(() -> getMvpView().addImage(filePath, currentImagePosition, request.getObjectKey()));  //request.getObjectKey()
                            Log.d("PutObject", "UploadSuccess " + request.getObjectKey());
                        }
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
                    getMvpView().post(() -> getMvpView().redirectToLogin());
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
