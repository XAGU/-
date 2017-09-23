package com.xiaolian.amigo.ui.ble.intf;


import com.polidea.rxandroidble.scan.ScanResult;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * Created by caidong on 2017/9/22.
 */
public interface IBleView extends IBaseView {

    // 添加设备
    void addDevice(ScanResult result);

    // 扫描设备失败
    void onScanError();

}
