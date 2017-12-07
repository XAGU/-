package com.xiaolian.amigo.ui.device.dispenser;

import android.os.ParcelUuid;
import android.text.TextUtils;

import com.xiaolian.amigo.data.manager.BleDataManager;
import com.xiaolian.amigo.util.Log;

import com.polidea.rxandroidble.scan.ScanResult;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IFavoriteManager;
import com.xiaolian.amigo.data.manager.intf.ITradeDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;
import com.xiaolian.amigo.data.network.model.dto.request.FavoriteReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ScanDeviceReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ScanDeviceRespDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenerView;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenserPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 选择饮水机
 * <p>
 * Created by zcd on 10/13/17.
 */

public class ChooseDispenserPresenter<V extends IChooseDispenerView> extends BasePresenter<V>
        implements IChooseDispenserPresenter<V> {

    private static final String TAG = ChooseDispenserPresenter.class.getSimpleName();
    private IFavoriteManager favoriteManager;
    private ITradeDataManager tradeDataManager;
    private IBleDataManager bleDataManager;
    private ISharedPreferencesHelp sharedPreferencesHelp;
    private int action;
    /**
     * 列表显示的是附近列表还是收藏列表
     * false 表示附近列表
     * true 表示收藏列表
     */
    private boolean listStatus = false;

    @Inject
    ChooseDispenserPresenter(IFavoriteManager favoriteManager,
                             ITradeDataManager tradeDataManager,
                             IBleDataManager bleDataManager,
                             ISharedPreferencesHelp sharedPreferencesHelp) {
        super();
        this.favoriteManager = favoriteManager;
        this.tradeDataManager = tradeDataManager;
        this.bleDataManager = bleDataManager;
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public void requestFavorites() {
        FavoriteReqDTO reqDTO = new FavoriteReqDTO();
        // 查看收藏设备列表
        addObserver(favoriteManager.queryFavorites(reqDTO), new NetworkObserver<ApiResult<ScanDeviceRespDTO>>(false, true) {
            @Override
            public void onReady(ApiResult<ScanDeviceRespDTO> result) {
                if (!isListStatus()) {
                    return;
                }
                getMvpView().completeRefresh();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (null != result.getData().getDevices() && result.getData().getDevices().size() > 0) {
                        List<ChooseDispenserAdaptor.DispenserWrapper> wrappers = new ArrayList<>();
                        for (ScanDeviceGroup device : result.getData().getDevices()) {
                            wrappers.add(new ChooseDispenserAdaptor.DispenserWrapper(device));
                        }
                        getMvpView().addMore(wrappers);
                    } else {
                        getMvpView().showEmptyView();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().showErrorView();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().showErrorView();
            }
        }, AndroidSchedulers.mainThread());
    }

    @Override
    public void onLoad() {
        closeBleConnection();
        resetSubscriptions();
        addObserver(bleDataManager.scan(), new BleObserver<ScanResult>() {
            // 已经上报的mac地址的集合
            List<String> existDevices = new ArrayList<>();
            // 新扫描到的mac地址的集合
            List<String> scanDevices = new ArrayList<>();

            Long begin = null;
            // 延时1s
            int delay = 1000;
            int maxDelay = 1000;

            @Override
            public void onNext(ScanResult result) {
                if (null == begin) {
                    // 起始时间设置为当前时间
                    begin = System.currentTimeMillis();
                }

                // 根据SERVICE_UUID筛选出可用设备
                boolean validDevice = false;
                if (null != result.getScanRecord() && null != result.getScanRecord().getServiceUuids()) {
                    for (ParcelUuid parcelUuid : result.getScanRecord().getServiceUuids()) {
                        if (parcelUuid.toString().equalsIgnoreCase(BleDataManager.SERVICE_UUID)) {
                            validDevice = true;
                            break;
                        }
                    }
                }

                if (!validDevice) {
                    return;
                }

                String macAddress = result.getBleDevice().getMacAddress();

                String[] temp = macAddress.split(":");
                StringBuilder deviceNo = new StringBuilder(temp[temp.length - 3]);
                deviceNo.append(temp[temp.length - 2]);
                deviceNo.append(temp[temp.length - 1]);
                if (TextUtils.isEmpty(sharedPreferencesHelp.getMacAddressByDeviceNo(deviceNo.toString()))) {
                    sharedPreferencesHelp.setDeviceNoAndMacAddress(deviceNo.toString(), macAddress);
                }

                if (!existDevices.contains(deviceNo.toString())
                        && !scanDevices.contains(deviceNo.toString())) { // 如果已经在上报的集合中，忽略
                    scanDevices.add(deviceNo.toString());
                }

                long now = System.currentTimeMillis();
                if (scanDevices.size() >= 10 || now - begin > delay) { // 列表数目到达10条或者时间超过2s都去服务端请求一次接口
                    if (scanDevices.size() > 0) {
                        if (delay < maxDelay) {
                            delay++;
                        }
                        existDevices.addAll(scanDevices);
                        handleScanDevices(new ArrayList<>(scanDevices)); // 请求服务器处理扫描到的设备
                        scanDevices.clear(); // 重置扫描到的设备集合
                        begin = now; // 重置计时器
                    }
                }
            }

            @Override
            public void onConnectError() {
                Log.e(TAG, "扫描设备失败");
            }

            @Override
            public void onExecuteError(Throwable e) {
                Log.wtf(TAG, "扫描设备失败", e);
            }
        }, Schedulers.io());
    }

    @Override
    public void closeBleConnection() {
        if (null != subscriptions && !subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
            subscriptions.clear();
        }
    }

    @Override
    public synchronized void setListStatus(boolean listStatus) {
        this.listStatus = listStatus;
    }

    @Override
    public void setAction(int action) {
        this.action = action;
    }

    @Override
    public int getAction() {
        return action;
    }

    @Override
    public void finishView() {
        getMvpView().finishView();
    }

    @Override
    public void gotoDispenser(String macAddress, boolean favor, Long residenceId,
                              String usefor, String location) {
        getMvpView().gotoDispenser(macAddress, favor, residenceId, usefor, location);
    }

    private synchronized boolean isListStatus() {
        return listStatus;
    }

    // 网络请求蓝牙扫描到的结果
    private void handleScanDevices(List<String> macAddresses) {
        ScanDeviceReqDTO scanDeviceReqDTO = new ScanDeviceReqDTO();
        scanDeviceReqDTO.setMacAddresses(macAddresses);
        addObserver(tradeDataManager.handleScanDevices(scanDeviceReqDTO), new NetworkObserver<ApiResult<ScanDeviceRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<ScanDeviceRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getDevices() != null && !result.getData().getDevices().isEmpty()) {
                        getMvpView().post(() -> getMvpView().completeRefresh());
                        getMvpView().post(() ->
                                getMvpView().addScanDevices(result.getData().getDevices()));
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        }, Schedulers.io());
    }
}
