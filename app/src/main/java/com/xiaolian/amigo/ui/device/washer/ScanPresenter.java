package com.xiaolian.amigo.ui.device.washer;

import android.text.TextUtils;

import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.manager.intf.IWasherDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanRespDTO;
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
    public void scanCheckout(String content) {
        QrCodeScanReqDTO reqDTO = new QrCodeScanReqDTO();
        reqDTO.setDeviceType(Device.WASHER.getType());
        reqDTO.setQrCodeData(content);
        // 1-绑定 2-扫描结账
        reqDTO.setPurpose(2);
        addObserver(washerDataManager.scanCheckout(reqDTO),
                new NetworkObserver<ApiResult<QrCodeScanRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<QrCodeScanRespDTO> result) {
                        if (null == result.getError()) {
                            washerDataManager.setDeviceToken(result.getData().getMacAddress(), result.getData().getDeviceToken());
                            getMvpView().gotoChooseModeView(result.getData().getBonus(),
                                    result.getData().getBalance(),
                                    result.getData().getMacAddress());
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
    public void checkDeviceUseage(int type) {
        DeviceCheckReqDTO reqDTO = new DeviceCheckReqDTO();
        reqDTO.setDeviceType(type);
        addObserver(washerDataManager.checkDeviceUseage(reqDTO), new NetworkObserver<ApiResult<DeviceCheckRespDTO>>() {

            @Override
            public void onReady(ApiResult<DeviceCheckRespDTO> result) {
                EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.ENABLE_VIEW));
                if (null == result.getError()) {
                    washerDataManager.saveDeviceCategory(result.getData().getDevices());
                    getMvpView().showDeviceUsageDialog(type, result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.ENABLE_VIEW));
            }
        });
    }

    @Override
    public boolean checkDefaultDormitoryExist() {
        return washerDataManager.getUserInfo().getResidenceId() != null;
    }


    @Override
    public void gotoHeaterDevice(String defaultAddress, Long defaultSupplierId, String location, Long residenceId) {
        if (TextUtils.isEmpty(defaultAddress)) {
            getMvpView().onError("二维码扫描失败");
        } else {
            getMvpView().gotoDevice(Device.HEATER, defaultAddress, defaultSupplierId,
                    location, residenceId, false);
        }
    }
}
