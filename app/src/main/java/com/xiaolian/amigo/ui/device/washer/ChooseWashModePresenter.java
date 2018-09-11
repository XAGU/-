package com.xiaolian.amigo.ui.device.washer;

import com.xiaolian.amigo.data.manager.intf.IWasherDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.trade.DryerModeRespDTO;
import com.xiaolian.amigo.data.network.model.trade.Mode;
import com.xiaolian.amigo.data.network.model.trade.PayReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeGenerateRespDTO;
import com.xiaolian.amigo.data.network.model.trade.WashingModeRespDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IChooseWashModePresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IChooseWashModeView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 选择洗衣机模式
 *
 * @author zcd
 * @date 18/1/12
 */

public class ChooseWashModePresenter<V extends IChooseWashModeView> extends BasePresenter<V>
        implements IChooseWashModePresenter<V> {
    private IWasherDataManager washerDataManager;
    private String deviceNo;

    @Inject
    ChooseWashModePresenter(IWasherDataManager washerDataManager) {
        this.washerDataManager = washerDataManager;
    }

    @Override
    public String getDeviceNo() {
        return deviceNo;
    }

    @Override
    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    @Override
    public void getWasherMode() {
        addObserver(washerDataManager.getWasherMode(),
                new NetworkObserver<ApiResult<WashingModeRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<WashingModeRespDTO> result) {
                        if (null == result.getError()) {
                            if (result.getData() != null && result.getData().getModes() != null) {
                                List<ChooseWashModeAdapter.WashModeItem> items = new ArrayList<>();
                                for (Mode mode : result.getData().getModes()) {
                                    items.add(new ChooseWashModeAdapter.WashModeItem(mode.getDesc(),
                                            mode.getPrice(), mode.getMode() ,4));
                                }
                                getMvpView().addMore(items);
                            }
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }

    @Override
    public void getDryerMode() {
        addObserver(washerDataManager.getDryerMode() ,new NetworkObserver<ApiResult<DryerModeRespDTO>>(){

            @Override
            public void onReady(ApiResult<DryerModeRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData() != null && result.getData().getModes() != null) {
                        List<ChooseWashModeAdapter.WashModeItem> items = new ArrayList<>();
                        for (Mode mode : result.getData().getModes()) {
                            items.add(new ChooseWashModeAdapter.WashModeItem(mode.getDesc(),
                                    mode.getPrice(), mode.getMode() ,6));
                        }
                        getMvpView().addMore(items);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void payAndGenerate(Long bonusId, String modeDesc, Double price, Integer mode) {
        PayReqDTO reqDTO = new PayReqDTO();
        reqDTO.setBonusId(bonusId);
        reqDTO.setMacAddress(deviceNo);
        reqDTO.setPrepay(price);
        reqDTO.setMode(mode);
        addObserver(washerDataManager.generateQRCode(reqDTO),
                new NetworkObserver<ApiResult<QrCodeGenerateRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<QrCodeGenerateRespDTO> result) {
                        if (result.getError() == null) {
                            getMvpView().gotoShowQRCodeView(result.getData().getQrCodeData(), modeDesc);
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }

    @Override
    public void getBalance() {
        addObserver(washerDataManager.getExtraInfo(), new NetworkObserver<ApiResult<PersonalExtraInfoDTO>>(false) {

            @Override
            public void onReady(ApiResult<PersonalExtraInfoDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getAllBalance() != null) {
                        getMvpView().refreshBalance(result.getData().getAllBalance());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    @Override
    public Double getLocalBalance() {
        return washerDataManager.getLocalBalance();
    }
}
