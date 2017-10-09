package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.ITradeDataManager;
import com.xiaolian.amigo.data.network.ITradeApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.ConnectCommandRespDTO;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 交易管理
 * <p>
 * Created by caidong on 2017/10/9.
 */
public class TradeDataManager implements ITradeDataManager {

    private ITradeApi tradeApi;

    @Inject
    public TradeDataManager(Retrofit retrofit) {
        tradeApi = retrofit.create(ITradeApi.class);
    }

    @Override
    public Observable<ApiResult<ConnectCommandRespDTO>> getConnectCommand() {
        return tradeApi.getConnectCommand();
    }
}
