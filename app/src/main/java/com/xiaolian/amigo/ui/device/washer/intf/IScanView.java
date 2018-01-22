package com.xiaolian.amigo.ui.device.washer.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 二维码扫描
 * <p>
 * Created by zcd on 18/1/17.
 */

public interface IScanView extends IBaseView {
    void gotoChooseModeView(String deviceNo);

    void resumeScan();
}
