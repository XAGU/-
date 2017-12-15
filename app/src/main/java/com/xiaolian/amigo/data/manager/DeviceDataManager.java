package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.data.network.ICsApi;
import com.xiaolian.amigo.data.network.IDeviceApi;
import com.xiaolian.amigo.data.network.IFundsApi;
import com.xiaolian.amigo.data.network.IOrderApi;
import com.xiaolian.amigo.data.network.ITradeApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.CmdResultReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ConnectCommandReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PayReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ScanDeviceReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.CmdResultRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ConnectCommandRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.CsMobileRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PayRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ScanDeviceRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.funds.PersonalWalletDTO;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.order.QueryPrepayOptionReqDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckReqDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckRespDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 设备数据管理
 * <p>
 * Created by zcd on 9/29/17.
 */

public class DeviceDataManager implements IDeviceDataManager {
    private static final String TAG = DeviceDataManager.class.getSimpleName();

    private ISharedPreferencesHelp sharedPreferencesHelp;
    private ITradeApi tradeApi;
    private IOrderApi orderApi;
    private IFundsApi fundsApi;
    private ICsApi csApi;
    private IDeviceApi deviceApi;

    @Inject
    public DeviceDataManager(Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        this.sharedPreferencesHelp  = sharedPreferencesHelp;
        tradeApi = retrofit.create(ITradeApi.class);
        orderApi = retrofit.create(IOrderApi.class);
        fundsApi = retrofit.create(IFundsApi.class);
        csApi = retrofit.create(ICsApi.class);
        deviceApi = retrofit.create(IDeviceApi.class);
    }
    @Override
    public void setBonusAmount(int amount) {
        sharedPreferencesHelp.setBonusAmount(amount);
    }

    @Override
    public int getBonusAmount() {
        return sharedPreferencesHelp.getBonusAmount();
    }

    @Override
    public String getMacAddressByDeviceNo(String macAddress) {
        return sharedPreferencesHelp.getMacAddressByDeviceNo(macAddress);
    }

    @Override
    public void setDeviceNoAndMacAddress(String macAddress, String currentMacAddress) {
        sharedPreferencesHelp.setDeviceNoAndMacAddress(macAddress, currentMacAddress);
    }

    @Override
    public void setDeviceToken(String deviceNo, String deviceToken) {
        sharedPreferencesHelp.setDeviceToken(deviceNo, deviceToken);
    }

    @Override
    public Observable<ApiResult<ConnectCommandRespDTO>> getConnectCommand(ConnectCommandReqDTO reqDTO) {
        return tradeApi.getConnectCommand(reqDTO);
    }

    @Override
    public Observable<ApiResult<CmdResultRespDTO>> processCmdResult(CmdResultReqDTO reqDTO) {
        return tradeApi.processCmdResult(reqDTO);
    }

    @Override
    public Observable<ApiResult<PayRespDTO>> pay(PayReqDTO reqDTO) {
        return tradeApi.pay(reqDTO);
    }

    @Override
    public Observable<ApiResult<ScanDeviceRespDTO>> handleScanDevices(ScanDeviceReqDTO reqDTO) {
        return tradeApi.handleScanDevices(reqDTO);
    }

    @Override
    public Observable<ApiResult<UnsettledOrderStatusCheckRespDTO>> checkOrderStatus(UnsettledOrderStatusCheckReqDTO reqDTO) {
        return orderApi.checkOrderStatus(reqDTO);
    }

    @Override
    public void setDeviceResult(String deviceNo, String deviceResult) {
        sharedPreferencesHelp.setDeviceResult(deviceNo, deviceResult);
    }

    @Override
    public String getDeviceResult(String deviceNo) {
        return sharedPreferencesHelp.getDeviceResult(deviceNo);
    }

    @Override
    public void setCloseCmd(String deviceNo, String closeCmd) {
        sharedPreferencesHelp.setCloseCmd(deviceNo, closeCmd);
    }

    @Override
    public String getCloseCmd(String deviceNo) {
        return sharedPreferencesHelp.getCloseCmd(deviceNo);
    }

    @Override
    public Observable<ApiResult<PersonalWalletDTO>> queryWallet() {
        return fundsApi.queryWallet();
    }

    @Override
    public Observable<ApiResult<OrderPreInfoDTO>> queryPrepayOption(QueryPrepayOptionReqDTO reqDTO) {
        return orderApi.queryPrepayOption(reqDTO);
    }

    @Override
    public Observable<ApiResult<CsMobileRespDTO>> queryCsInfo() {
        return csApi.queryCsInfo();
    }

    @Override
    public boolean isHeaterGuideDone() {
        return sharedPreferencesHelp.isHeaterGuideDone();
    }

    @Override
    public void doneHeaterGuide() {
        sharedPreferencesHelp.doneHeaterGuide();
    }

    @Override
    public boolean isDispenserGuideDone() {
        return sharedPreferencesHelp.isDispenserGuideDone();
    }

    @Override
    public void doneDispenserGuide() {
        sharedPreferencesHelp.doneDispenserGuide();
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> favorite(SimpleReqDTO reqDTO) {
        return deviceApi.favorite(reqDTO);
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> unFavorite(SimpleReqDTO reqDTO) {
        return deviceApi.unFavorite(reqDTO);
    }
}
