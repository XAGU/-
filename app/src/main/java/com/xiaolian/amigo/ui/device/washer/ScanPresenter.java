package com.xiaolian.amigo.ui.device.washer;

import android.text.TextUtils;

import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.manager.intf.IWasherDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.BriefDeviceDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.device.GetDeviceDetailReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanRespDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IScanPresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IScanView;
import com.xiaolian.amigo.ui.main.HomeFragment2;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * 二维码扫描
 *
 * @author zcd
 * @date 18/1/17
 */

public class ScanPresenter<V extends IScanView> extends BasePresenter<V>
        implements IScanPresenter<V> {
    private IWasherDataManager washerDataManager;

    @Inject
    ScanPresenter(IWasherDataManager washerDataManager) {
        this.washerDataManager = washerDataManager;
    }

    @Override
    public void scanCheckout(String content , int type) {
        QrCodeScanReqDTO reqDTO = new QrCodeScanReqDTO();
        if (type != -1) {
            reqDTO.setDeviceType(type);
        }
        reqDTO.setQrCodeData(content);
        // 1-绑定 2-扫描结账
        reqDTO.setPurpose(2);
        addObserver(washerDataManager.scanCheckout(reqDTO),
                new NetworkObserver<ApiResult<QrCodeScanRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<QrCodeScanRespDTO> result) {
                        if (null == result.getError()) {
                            if (type != -1) {//兼容老设备，这个接口会返回设备类型
                                washerDataManager.setDeviceToken(result.getData().getMacAddress(), result.getData().getDeviceToken());
                                getMvpView().gotoChooseModeView(result.getData().getBonus(),
                                        result.getData().getBalance(),
                                        result.getData().getMacAddress(), type);
                            }else{
                                getMvpView().goToWasher(result.getData().getDeviceToken()
                                ,result.getData().getMacAddress()
                                ,result.getData().getDeviceType());

                            }
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                            getMvpView().resumeScan();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().resumeScan();
                    }
                });
    }

    @Override
    public void checkDeviceUseage(int type , String mac ,boolean isBle) {
        DeviceCheckReqDTO reqDTO = new DeviceCheckReqDTO();
        reqDTO.setDeviceType(type);
        addObserver(washerDataManager.checkDeviceUseage(reqDTO), new NetworkObserver<ApiResult<DeviceCheckRespDTO>>() {

            @Override
            public void onReady(ApiResult<DeviceCheckRespDTO> result) {
                EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.ENABLE_VIEW));
                if (null == result.getError()) {
                    washerDataManager.saveDeviceCategory(result.getData().getDevices());
                    getMvpView().showDeviceUsageDialog(type, result.getData() ,mac ,isBle);
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().resumeScan();
                }
            }
            
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().resumeScan();
            }
        });
    }

    @Override
    public boolean checkDefaultDormitoryExist() {
        return washerDataManager.getUserInfo().getResidenceId() != null;
    }

    @Override
    public void gotoHeaterDevice(String defaultAddress, Long defaultSupplierId, String location ,long reseniceId) {
        if (TextUtils.isEmpty(defaultAddress)) {
            getMvpView().onError("二维码扫描失败");
        } else {
            getMvpView().gotoDevice(Device.HEATER, defaultAddress, defaultSupplierId,location ,reseniceId
                    , false);
        }
    }

    @Override
    public void getDeviceDetail(boolean isTimeValid,int type,String macAddress, boolean isBle) {
        GetDeviceDetailReqDTO getDeviceDetailReqDTO = new GetDeviceDetailReqDTO();
        getDeviceDetailReqDTO.setMacAddress(macAddress);
        addObserver(washerDataManager.getDeviceDetail(getDeviceDetailReqDTO) ,new NetworkObserver<ApiResult<BriefDeviceDTO>>(){

            @Override
            public void onReady(ApiResult<BriefDeviceDTO> result) {
                if (result.getError() == null){
                    getMvpView().goToBleDevice( isTimeValid,result.getData().getDeviceType() ,macAddress,result.getData() ,isBle);
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public User getUserInfo() {
        return washerDataManager.getUserInfo();
    }

    @Override
    public String getToken(){return washerDataManager.getToken();}
}
