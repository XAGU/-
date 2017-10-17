package com.xiaolian.amigo.ui.lostandfound;

import android.net.Uri;

import com.xiaolian.amigo.data.enumeration.LostAndFound;
import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.SaveLostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostView;
import com.xiaolian.amigo.util.Constant;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 失物发布
 * <p>
 * Created by zcd on 9/21/17.
 */

public class PublishLostPresenter<V extends IPublishLostView> extends BasePresenter<V>
        implements IPublishLostPresenter<V> {
    private static final String TAG = PublishLostPresenter.class.getSimpleName();
    private ILostAndFoundDataManager lostAndFoundManager;
    private IUserDataManager userManager;

    @Inject
    public PublishLostPresenter(ILostAndFoundDataManager lostAndFoundManager, IUserDataManager userManager) {
        super();
        this.lostAndFoundManager = lostAndFoundManager;
        this.userManager = userManager;
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
    public void uploadImage(Uri imageUri, int position) {
        RequestBody image = RequestBody.create(MediaType.parse(Constant.UPLOAD_IMAGE_CONTENT_TYPE),
                new File(imageUri.getPath()));
        addObserver(userManager.uploadFile(image), new NetworkObserver<ApiResult<String>>() {

            @Override
            public void onReady(ApiResult<String> result) {
                if (null == result.getError()) {
                    getMvpView().addImage(result.getData(), position);
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
