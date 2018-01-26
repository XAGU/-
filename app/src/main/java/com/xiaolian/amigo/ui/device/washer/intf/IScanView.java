package com.xiaolian.amigo.ui.device.washer.intf;

import com.xiaolian.amigo.data.vo.Bonus;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 二维码扫描
 * <p>
 * Created by zcd on 18/1/17.
 */

public interface IScanView extends IBaseView {
    void gotoChooseModeView(Bonus bonus, Double balance, String deviceNo);

    void resumeScan();
}
