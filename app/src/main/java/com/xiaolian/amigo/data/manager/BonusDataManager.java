package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IBonusDataManager;
import com.xiaolian.amigo.data.network.IBonusApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bonus.QueryUserBonusReqDTO;
import com.xiaolian.amigo.data.network.model.bonus.RedeemBonusReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.bonus.QueryUserBonusListRespDTO;
import com.xiaolian.amigo.di.UserServer;

import javax.inject.Inject;

import rx.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
 * 代金券DataManager实现类
 *
 * @author zcd
 * @date 17/9/18
 */

public class BonusDataManager implements IBonusDataManager {

    @SuppressWarnings("unused")
    private static final String TAG = BonusDataManager.class.getSimpleName();

    private IBonusApi bonusApi;

    @Inject
    public BonusDataManager(@UserServer Retrofit retrofit) {
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
