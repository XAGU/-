package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.ITradeDataManager;
import com.xiaolian.amigo.data.network.ITradeApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.CmdResultReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ConnectCommandReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PayReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.CmdResultRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ConnectCommandRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PayRespDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;

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
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public TradeDataManager(Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        tradeApi = retrofit.create(ITradeApi.class);
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public Observable<ApiResult<ConnectCommandRespDTO>> getConnectCommand(ConnectCommandReqDTO reqDTO) {
        sharedPreferencesHelp.setCurrentDeviceToken(sharedPreferencesHelp.getDeviceToken(reqDTO.getMacAddress()));
        return tradeApi.getConnectCommand(reqDTO);
    }

    @Override
    public Observable<ApiResult<CmdResultRespDTO>> processCmdResult(@Body CmdResultReqDTO reqDTO) {
        sharedPreferencesHelp.setCurrentDeviceToken(sharedPreferencesHelp.getDeviceToken(reqDTO.getMacAddress()));
        return tradeApi.processCmdResult(reqDTO);
    }

    @Override
    public Observable<ApiResult<PayRespDTO>> pay(@Body PayReqDTO reqDTO) {
        sharedPreferencesHelp.setCurrentDeviceToken(sharedPreferencesHelp.getDeviceToken(reqDTO.getMacAddress()));
        return tradeApi.pay(reqDTO);
    }
}
