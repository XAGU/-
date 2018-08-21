package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IWasherDataManager;
import com.xiaolian.amigo.data.network.IDeviceApi;
import com.xiaolian.amigo.data.network.ITradeApi;
import com.xiaolian.amigo.data.network.IUserApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.DeviceCategoryBO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.trade.PayReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeGenerateRespDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanRespDTO;
import com.xiaolian.amigo.data.network.model.trade.WashingModeRespDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.data.vo.DeviceCategory;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.di.UserServer;
import com.xiaolian.amigo.util.Log;

import java.util.ArrayList;
import java.util.List;

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
    private IDeviceApi deviceApi ;

    @Inject
    public WasherDataManager(@UserServer Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        this.sharedPreferencesHelp = sharedPreferencesHelp;
        tradeApi = retrofit.create(ITradeApi.class);
        userApi = retrofit.create(IUserApi.class);
        deviceApi = retrofit.create(IDeviceApi.class);
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

    @Override
    public Observable<ApiResult<DeviceCheckRespDTO>> checkDeviceUseage(DeviceCheckReqDTO reqDTO) {
        return deviceApi.checkDeviceUseage(reqDTO);
    }

    @Override
    public void saveDeviceCategory(List<DeviceCategoryBO> devices) {
        if (devices == null) {
            return;
        }
        List<DeviceCategory> deviceCategories = new ArrayList<>();
        for (DeviceCategoryBO bo : devices) {
            deviceCategories.add(bo.transform());
        }
        sharedPreferencesHelp.saveDeviceCategory(deviceCategories);
    }

    @Override
    public User getUserInfo() {
        return sharedPreferencesHelp.getUserInfo();
    }
}
