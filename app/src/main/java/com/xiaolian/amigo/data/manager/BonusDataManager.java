package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IBonusDataManager;
import com.xiaolian.amigo.data.network.IBonusApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryUserBonusReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RedeemBonusReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryUserBonusListRespDTO;

import javax.inject.Inject;

import rx.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
 * 红包DataManager实现类
 * @author zcd
 */

public class BonusDataManager implements IBonusDataManager {

    private static final String TAG = BonusDataManager.class.getSimpleName();

    private IBonusApi bonusApi;
    @Inject
    public BonusDataManager(Retrofit retrofit) {
        bonusApi = retrofit.create(IBonusApi.class);
    }

    @Override
    public Observable<ApiResult<QueryUserBonusListRespDTO>> queryOrders(@Body QueryUserBonusReqDTO reqDTO) {
        return bonusApi.queryOrders(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> redeem(@Body RedeemBonusReqDTO reqDTO) {
        return bonusApi.redeem(reqDTO);
    }
}
