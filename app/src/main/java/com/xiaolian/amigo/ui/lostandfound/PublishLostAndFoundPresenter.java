package com.xiaolian.amigo.ui.lostandfound;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.IntDef;

import com.xiaolian.amigo.data.enumeration.LostAndFound;
import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.manager.intf.IOssDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostAndFoundDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostAndFoundView;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

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
    public void uploadImage(Context activity, Uri imageUri, int position, OssFileType found) {

    }

    @Override
    public void publishLostAndFound(String desc, List<String> images, String title, int type) {
        SaveLostAndFoundDTO dto = new SaveLostAndFoundDTO();
        dto.setDescription(desc);
        dto.setImages(images);
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
}
