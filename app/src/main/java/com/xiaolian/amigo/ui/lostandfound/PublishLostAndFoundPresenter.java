package com.xiaolian.amigo.ui.lostandfound;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.base.OssClientHolder;
import com.xiaolian.amigo.data.enumeration.LostAndFound;
import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.manager.intf.IOssDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.file.OssModel;
import com.xiaolian.amigo.data.network.model.lostandfound.BbsTopicListTradeRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostAndFoundDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostAndFoundView;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import retrofit2.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zcd
 * @date 18/5/14
 */
public class PublishLostAndFoundPresenter<V extends IPublishLostAndFoundView>
    extends BasePresenter<V> implements IPublishLostAndFoundPresenter<V> {
    private static final String TAG = PublishLostAndFoundPresenter.class.getSimpleName();
    private ILostAndFoundDataManager lostAndFoundManager;
    private IOssDataManager ossDataManager;
    private Random random = new Random();
    private int currentImagePosition;
    private OssFileType currentType;

    @Inject
    public PublishLostAndFoundPresenter(ILostAndFoundDataManager lostAndFoundManager, IOssDataManager ossDataManager) {
        this.lostAndFoundManager = lostAndFoundManager;
        this.ossDataManager = ossDataManager;
    }

    @Override
    public void uploadImage(Context context, Uri imageUri, int position, OssFileType type) {
        currentImagePosition = position;
        currentType = type;
        uploadImage(context, imageUri.getPath());
    }

    @Override
    public void publishLostAndFound(String desc, List<String> images, String title, int type) {
        SaveLostAndFoundDTO dto = new SaveLostAndFoundDTO();
        dto.setDescription(desc);
        dto.setImages(images);
        dto.setTitle(title);
        dto.setType(1);
        dto.setTopicId(type);
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
    public List<BbsTopicListTradeRespDTO.TopicListBean> getTopicList() {
        return lostAndFoundManager.getTopic();
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
                        getMvpView().post(() -> getMvpView().addImage(request.getObjectKey(), currentImagePosition));
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
        return currentType.getDesc() + "/" + lostAndFoundManager.getUserInfo().getId() + "_"
                + serverTime + "_" + generateRandom() + ".jpg";
    }

    private String generateRandom() {
        int max = 9999;
        int min = 0;
        int num = random.nextInt(max) % (max - min + 1) + min;
        return String.format(Locale.getDefault(), "%04d", num);
    }
}
