package com.xiaolian.amigo.data.manager.intf;

import com.polidea.rxandroidble.scan.ScanResult;

import rx.Observable;

/**
 * 蓝牙相关操作
 * <p>
 * Created by caidong on 2017/9/22.
 */
public interface IBLEDataManager {

    // 扫描设备
    Observable<ScanResult> scan();
}
