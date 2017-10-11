package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.ITradeDataManager;
import com.xiaolian.amigo.data.network.ITradeApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.CmdResultReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PayReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.CmdResultRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ConnectCommandRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PayRespDTO;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.Body;
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

    @Override
    public Observable<ApiResult<CmdResultRespDTO>> processCmdResult(@Body CmdResultReqDTO reqDTO) {
        return tradeApi.processCmdResult(reqDTO);
    }

    @Override
    public Observable<ApiResult<PayRespDTO>> pay(@Body PayReqDTO reqDTO) {
        return tradeApi.pay(reqDTO);
    }
}
