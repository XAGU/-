package com.xiaolian.amigo.ui.device.dispenser;

import android.os.CountDownTimer;
import android.os.ParcelUuid;
import android.text.TextUtils;

import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.DeviceDTO;
import com.xiaolian.amigo.data.network.model.device.FavorDeviceDTO;
import com.xiaolian.amigo.data.network.model.device.QueryDeviceListReqDTO;
import com.xiaolian.amigo.data.network.model.device.QueryDeviceListRespDTO;
import com.xiaolian.amigo.data.network.model.device.QueryFavorDeviceRespDTO;
import com.xiaolian.amigo.data.network.model.device.Supplier;
import com.xiaolian.amigo.data.vo.DeviceCategory;
import com.xiaolian.amigo.data.vo.ScanDeviceGroup;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenerView;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenserPresenter;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.blelib.BluetoothConstants;
import com.xiaolian.blelib.ScanRecord;
import com.xiaolian.blelib.scan.BluetoothScanResponse;
import com.xiaolian.blelib.scan.BluetoothScanResult;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 选择饮水机
 *
 * @author zcd
 * @date 10/13/17
 */

public class ChooseDispenserPresenter<V extends IChooseDispenerView> extends BasePresenter<V>
        implements IChooseDispenserPresenter<V> {

    private static final String TAG = ChooseDispenserPresenter.class.getSimpleName();
    /**
     * 扫描列表数目到达10条上传
     */
    private static final int MAX_SCAN_SIZE = 10;
    private IBleDataManager bleDataManager;
    private IDeviceDataManager deviceDataManager;
    private List<DeviceCategory> deviceCategories;
    private int action;
    private CountDownTimer timer;
    private Integer deviceType;
    /**
     * 列表显示的是附近列表还是收藏列表
     * false 表示附近列表
     * true 表示收藏列表
     */
    private boolean listStatus = false;
    private int scanType = BluetoothConstants.SCAN_TYPE_BLE;

    @Inject
    ChooseDispenserPresenter(IBleDataManager bleDataManager, IDeviceDataManager deviceDataManager) {
        super();
        this.bleDataManager = bleDataManager;
        this.deviceDataManager = deviceDataManager;
        this.deviceCategories = deviceDataManager.getDeviceCategory();
        this.scanType = deviceDataManager.getScanType();
    }

    @Override
    public void requestFavorites() {
        cancelTimer();
        QueryDeviceListReqDTO reqDTO = new QueryDeviceListReqDTO();
        reqDTO.setType(deviceType);
        // 查看收藏设备列表
        addObserver(deviceDataManager.getFavorites(reqDTO), new NetworkObserver<ApiResult<QueryFavorDeviceRespDTO>>(false, true) {
            @Override
            public void onReady(ApiResult<QueryFavorDeviceRespDTO> result) {
                if (!isListStatus()) {
                    return;
                }
                getMvpView().completeRefresh();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (null != result.getData().getDevices() && result.getData().getDevices().size() > 0) {
                        List<ChooseDispenserAdaptor.DispenserWrapper> wrappers = new ArrayList<>();
                        for (FavorDeviceDTO device : result.getData().getDevices()) {
                            wrappers.add(new ChooseDispenserAdaptor.DispenserWrapper(device.transform()));
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
        startTimer();
        closeBleConnection();
        resetSubscriptions();
        Log.d(TAG, "开始扫描设备");
        bleDataManager.scan(scanType, new BluetoothScanResponse() {
            // 已经上报的mac地址的集合
            List<String> existDevices = new ArrayList<>();
            // 新扫描到的mac地址的集合
            List<String> scanDevices = new ArrayList<>();

            Long begin = null;
            // 延时1s
            int delay = 1000;
            int maxDelay = 1000;
            @Override
            public void onScanStarted() {
                Log.d(TAG, "onScanStarted");
            }

            @Override
            public void onDeviceFounded(BluetoothScanResult result) {
                if (null == begin) {
                    // 起始时间设置为当前时间
                    begin = System.currentTimeMillis();
                    Log.d(TAG, "可以扫描到设备，缓存当前的扫描方式" + scanType);
                    // 可以扫描到设备，缓存当前的扫描方式
                    deviceDataManager.saveScanType(scanType);
                }


                if (!checkDeviceValid(result)) {
                    return;
                }

                String macAddress = result.getAddress();
                String deviceNo = result.getName();
                if (TextUtils.isEmpty(deviceDataManager.getMacAddressByDeviceNo(deviceNo))) {
                    deviceDataManager.setDeviceNoAndMacAddress(deviceNo, macAddress);
                }

                // 如果已经在上报的集合中，忽略
                if (!existDevices.contains(deviceNo)
                        && !scanDevices.contains(deviceNo)) {
                    scanDevices.add(deviceNo);
                }

                long now = System.currentTimeMillis();
                // 列表数目到达10条或者时间超过2s都去服务端请求一次接口
                if (scanDevices.size() >= MAX_SCAN_SIZE || now - begin > delay) {
                    if (scanDevices.size() > 0) {
                        if (delay < maxDelay) {
                            delay++;
                        }
                        existDevices.addAll(scanDevices);
                        // 请求服务器处理扫描到的设备
                        handleScanDevices(new ArrayList<>(scanDevices));
                        // 重置扫描到的设备集合
                        scanDevices.clear();
                        // 重置计时器
                        begin = now;
                    }
                }

            }

            @Override
            public void onScanStopped() {
                Log.d(TAG, "onScanStopped");
            }

            @Override
            public void onScanCanceled() {
                Log.d(TAG, "onScanCanceled");
            }
        });
    }

    private boolean checkDeviceValid(BluetoothScanResult result) {
        if (scanType == BluetoothConstants.SCAN_TYPE_CLASSIC) {
            return true;
        }
        // 根据SERVICE_UUID筛选出可用设备
        boolean validDevice = false;
        ScanRecord scanRecord = ScanRecord.parseFromBytes(result.getScanRecord());
        if (null != scanRecord && null != scanRecord.getServiceUuids()) {
            for (ParcelUuid parcelUuid : scanRecord.getServiceUuids()) {
                for (DeviceCategory deviceCategory : deviceCategories) {
                    for (Supplier s : deviceCategory.getSuppliers()) {
                        if (parcelUuid.toString().equalsIgnoreCase(s.getServiceUuid())) {
                            validDevice = true;
                            break;
                        }
                    }
                    if (validDevice) {
                        break;
                    }
                }
                if (validDevice) {
                    break;
                }
            }
        }
        return validDevice;
    }

    @Override
    public void closeBleConnection() {
        bleDataManager.stopScan();
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
    public void gotoDispenser(String macAddress, Long supplierId,
                              boolean favor, Long residenceId,
                              String usefor, String location) {
        getMvpView().gotoDispenser(macAddress, supplierId, favor, residenceId, usefor, location);
    }

    @Override
    public void toggleScanType() {
        if (scanType == BluetoothConstants.SCAN_TYPE_CLASSIC) {
            scanType = BluetoothConstants.SCAN_TYPE_BLE;
        } else if (scanType == BluetoothConstants.SCAN_TYPE_BLE) {
            scanType = BluetoothConstants.SCAN_TYPE_CLASSIC;
        } else {
            scanType = BluetoothConstants.SCAN_TYPE_BLE;
        }
    }

    @Override
    public void gotoDryer(String deviceNo, Long supplierId, Boolean isFavor, Long residenceId, String location) {
        getMvpView().gotoDryer(deviceNo, supplierId, isFavor, residenceId, location);
    }

    @Override
    public void startTimer() {
        if (timer != null) {
            timer.start();
            return;
        }
        timer = new CountDownTimer(Constant.DEVICE_SCAN_TIMEOUT * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (getMvpView() != null) {
                    getMvpView().post(() -> getMvpView().showScanStopView());
                }
            }
        };
        timer.start();
    }

    @Override
    public void cancelTimer() {
        if (null != timer) {
            timer.cancel();
        }
    }

    @Override
    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    private synchronized boolean isListStatus() {
        return listStatus;
    }

    /**
     * 网络请求蓝牙扫描到的结果
     *
     * @param macAddresses 扫描到设备mac地址
     */
    private void handleScanDevices(List<String> macAddresses) {
        QueryDeviceListReqDTO reqDTO = new QueryDeviceListReqDTO();
        reqDTO.setType(deviceType);
        reqDTO.setMacAddresses(macAddresses);
        addObserver(deviceDataManager.handleScanDevices(reqDTO), new NetworkObserver<ApiResult<QueryDeviceListRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<QueryDeviceListRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getDevices() != null && !result.getData().getDevices().isEmpty()) {
                        List<ScanDeviceGroup> devices = new ArrayList<>();
                        for (DeviceDTO deviceDTO : result.getData().getDevices()) {
                            devices.add(deviceDTO.transform());
                        }
                        getMvpView().post(() -> getMvpView().completeRefresh());
                        getMvpView().post(() ->
                                getMvpView().addScanDevices(devices));
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
