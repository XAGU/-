package com.xiaolian.amigo.ui.device.dispenser;

import android.util.Log;

import com.polidea.rxandroidble.scan.ScanResult;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IFavoriteManager;
import com.xiaolian.amigo.data.manager.intf.ITradeDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.Device;
import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;
import com.xiaolian.amigo.data.network.model.dto.request.FavoriteReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ScanDeviceReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.FavoriteRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ScanDeviceRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenerView;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenserPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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

    @Inject
    public ChooseDispenserPresenter(IFavoriteManager favoriteManager, ITradeDataManager tradeDataManager, IBleDataManager bleDataManager) {
        super();
        this.favoriteManager = favoriteManager;
        this.tradeDataManager = tradeDataManager;
        this.bleDataManager = bleDataManager;
    }

    @Override
    public void requestFavorites() {
        FavoriteReqDTO reqDTO = new FavoriteReqDTO();
        // 查看收藏设备列表
        addObserver(favoriteManager.queryFavorites(reqDTO), new NetworkObserver<ApiResult<ScanDeviceRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<ScanDeviceRespDTO> result) {
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
        });
    }

    @Override
    public void onLoad() {
        addObserver(bleDataManager.scan(), new BleObserver<ScanResult>() {
            // 已经上报的mac地址的集合
            List<String> existDevices = new ArrayList<String>();
            // 新扫描到的mac地址的集合
            List<String> scanDevices = new ArrayList<String>();

            Long begin = null;

            @Override
            public void onNext(ScanResult result) {
                if (null == begin) {
                    // 起始时间设置为当前时间
                    begin = System.currentTimeMillis();
                }

                String macAddress = result.getBleDevice().getMacAddress();

                if (!existDevices.contains(macAddress) && !scanDevices.contains(macAddress)) { // 如果已经在上报的集合中，忽略
                    scanDevices.add(macAddress);
                }

                long now = System.currentTimeMillis();
                if (scanDevices.size() >= 10 || now - begin > 2000) { // 列表数目到达10条或者时间超过2s都去服务端请求一次接口
                    if (scanDevices.size() > 0) {
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

    // 网络请求蓝牙扫描到的结果
    private void handleScanDevices(List<String> macAddresses) {
        ScanDeviceReqDTO scanDeviceReqDTO = new ScanDeviceReqDTO();
        scanDeviceReqDTO.setMacAddresses(macAddresses);
        addObserver(tradeDataManager.handleScanDevices(scanDeviceReqDTO), new NetworkObserver<ApiResult<ScanDeviceRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<ScanDeviceRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().addScanDevices(result.getData().getDevices());
                }
            }
        }, Schedulers.io());
    }
}
