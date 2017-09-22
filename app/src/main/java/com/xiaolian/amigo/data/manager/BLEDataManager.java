package com.xiaolian.amigo.data.manager;

import android.os.ParcelUuid;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;
import com.xiaolian.amigo.data.manager.intf.IBLEDataManager;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by caidong on 2017/9/22.
 */
public class BLEDataManager implements IBLEDataManager {

    private static final String SERVICE_UUID = "0000fee9-0000-1000-8000-00805f9b34fb";
    private RxBleClient client;

    @Inject
    public BLEDataManager(RxBleClient client) {
        this.client = client;
    }

    @Override
    public Observable<ScanResult> scan() {
        return client.scanBleDevices(new ScanSettings.Builder().build(),
                new ScanFilter.Builder()
                        // 按照SERVICE_UUID筛选
//                        .setServiceUuid(ParcelUuid.fromString(SERVICE_UUID))
                        .build());
    }
}
