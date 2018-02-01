package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IOssDataManager;
import com.xiaolian.amigo.data.network.IOssApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.file.OssModel;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * oss data manager
 *
 * @author zcd
 * @date 17/11/13
 */

public class OssDataManager implements IOssDataManager {
    private static final String TAG = OssDataManager.class.getSimpleName();
    private IOssApi ossApi;

    @Inject
    public OssDataManager(Retrofit retrofit) {
        this.ossApi = retrofit.create(IOssApi.class);
    }

    @Override
    public Observable<ApiResult<OssModel>> getOssModel() {
        return ossApi.getOssModel();
    }
}
