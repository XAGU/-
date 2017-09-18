package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IRepairDataManager;
import com.xiaolian.amigo.data.network.IRepairApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.RepairReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairRespDTO;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
 * 报修管理
 * <p>
 * Created by caidong on 2017/9/18.
 */
public class RepairDataManager implements IRepairDataManager{

    private static final String TAG = RepairDataManager.class.getSimpleName();

    private IRepairApi repairApi;

    @Inject
    public RepairDataManager(Retrofit retrofit) {
        repairApi = retrofit.create(IRepairApi.class);
    }

    @Override
    public Observable<ApiResult<RepairRespDTO>> queryRepairs(@Body RepairReqDTO reqDTO) {
        return repairApi.queryRepairs(reqDTO);
    }
}
