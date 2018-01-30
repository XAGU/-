package com.xiaolian.amigo.ui.device.washer.intf;

import com.xiaolian.amigo.data.vo.Bonus;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 二维码扫描
 *
 * @author zcd
 * @date 18/1/17
 */

public interface IScanView extends IBaseView {
    /**
     * 跳转到选择模式页面
     *
     * @param bonus    红包
     * @param balance  余额
     * @param deviceNo 设备编号
     */
    void gotoChooseModeView(Bonus bonus, Double balance, String deviceNo);

    /**
     * 恢复扫描
     */
    void resumeScan();
}
