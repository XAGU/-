package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IWasherDataManager;
import com.xiaolian.amigo.data.network.ITradeApi;
import com.xiaolian.amigo.data.network.IUserApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.trade.PayReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeGenerateRespDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanRespDTO;
import com.xiaolian.amigo.data.network.model.trade.WashingModeRespDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.di.UserServer;
import com.xiaolian.amigo.util.Log;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 洗衣机模块
 *
 * @author zcd
 * @date 18/1/12
 */

public class WasherDataManager implements IWasherDataManager {
    private static final String TAG = WasherDataManager.class.getSimpleName();
    private ITradeApi tradeApi;
    private IUserApi userApi;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public WasherDataManager(@UserServer Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        this.sharedPreferencesHelp = sharedPreferencesHelp;
        tradeApi = retrofit.create(ITradeApi.class);
        userApi = retrofit.create(IUserApi.class);
    }

    @Override
    public Observable<ApiResult<QrCodeScanRespDTO>> scanCheckout(QrCodeScanReqDTO reqDTO) {
        return tradeApi.scanCheckout(reqDTO);
    }

    @Override
    public Observable<ApiResult<QrCodeGenerateRespDTO>> generateQRCode(PayReqDTO reqDTO) {
        sharedPreferencesHelp.setCurrentDeviceToken(sharedPreferencesHelp.getDeviceToken(reqDTO.getMacAddress()));
        return tradeApi.generateQRCode(reqDTO);
    }

    @Override
    public Observable<ApiResult<WashingModeRespDTO>> getWasherMode() {
        return tradeApi.getWasherMode();
    }

    @Override
    public void setDeviceToken(String deviceNo, String deviceToken) {
        sharedPreferencesHelp.setDeviceToken(deviceNo, deviceToken);
    }

    @Override
    public Observable<ApiResult<PersonalExtraInfoDTO>> getExtraInfo() {
        return userApi.getUserExtraInfo();
    }

    @Override
    public Double getLocalBalance() {
        String balance = sharedPreferencesHelp.getBalance();
        try {
            return Double.valueOf(balance);
        } catch (Exception e) {
            Log.wtf(TAG, e);
            return 0.0;
        }
    }
}
