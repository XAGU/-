package com.xiaolian.amigo.ui.user;

import android.content.Context;
import android.content.Intent;
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
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.file.OssModel;
import com.xiaolian.amigo.data.network.model.user.UserAuthCertifyReqDTO;
import com.xiaolian.amigo.data.network.model.user.UserGradeInfoRespDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationPresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationView;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.HttpException;
import retrofit2.http.Multipart;
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
    UserCertificationPresenter(IUserDataManager userDataManager ,IOssDataManager ossDataManager){
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
        getMvpView().post(() -> getMvpView().addImage(imagePath ,position ,""));
    }

    @Override
    public void uploadImage(Context activity, String imagePath, OssFileType found, ImageView iv) {
        getMvpView().post(() -> getMvpView().setCardImage(iv ,imagePath ,""));
    }

    @Override
    public String getDormitory() {
        User user = userDataManager.getUser();
        if (user != null){
            return user.getResidenceName();
        }else{
            return "";
        }
    }

    @Override
    public void getGradeInfo() {
        addObserver(userDataManager.gradeInfo() ,new NetworkObserver<ApiResult<UserGradeInfoRespDTO>>(){

            @Override
            public void onReady(ApiResult<UserGradeInfoRespDTO> result) {
                if (result.getError() == null){
                    getMvpView().setGradeInfo(result.getData());
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }


    @Override
    public void certify(String className, String faculty, Integer grade, File idCardBehind, File idCardFront, String major, Long stuNum, List<File> stuPictureUrls) {
        RequestBody requestBody ;
        MultipartBody.Builder builder ;

        builder= new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("className" ,className)
                .addFormDataPart("faculty" ,faculty)
                .addFormDataPart("grade" ,grade+"")
                .addFormDataPart("major" , major)
                .addFormDataPart("stuNum" , stuNum+"")
                .addFormDataPart("idCardBehindPicture" ,idCardBehind.getName() ,RequestBody.create(MediaType.parse("image/*") , idCardBehind))
                .addFormDataPart("idCardFrontPicture" ,idCardFront.getName() , RequestBody.create(MediaType.parse("image/*") ,idCardFront));

        for (File file : stuPictureUrls){
            if (file.exists())
                builder.addFormDataPart("stuPictures",file.getName() ,RequestBody.create(MediaType.parse("image/*") ,file));
        }
        requestBody = builder.build();

        addObserver(userDataManager.certify(requestBody) ,new NetworkObserver<ApiResult<BooleanRespDTO>>(){

            @Override
            public void onStart() {
                getMvpView().showUpDialog();
            }

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {


                 if (result.getError() == null){
                     if (result.getData().isResult()){
//                         getMvpView().onSuccess("认证成功");
//                         getMvpView().certifySuccess();
                         getMvpView().hideSuccessDialog();
                     }else{
                         getMvpView().onError(result.getError().getDisplayMessage());
                         getMvpView().hideFailureDialgo();
                     }
                 }else{
                     getMvpView().onError(result.getError().getDisplayMessage());
                     getMvpView().hideFailureDialgo();
                 }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().hideFailureDialgo();
            }
        });
    }

    @Override
    public void setCertifyStatus(int certificationReviewing) {
        userDataManager.setCertifyStatus(certificationReviewing);
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
