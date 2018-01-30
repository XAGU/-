package com.xiaolian.amigo.ui.device.washer.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.device.washer.ChooseWashModeAdapter;

import java.util.List;

/**
 * 选择洗衣机模式
 *
 * @author zcd
 * @date 18/1/12
 */

public interface IChooseWashModeView extends IBaseView {
    /**
     * 更新模式列表
     *
     * @param items 列表元素
     */
    void addMore(List<ChooseWashModeAdapter.WashModeItem> items);

    /**
     * 跳转到显示二维码页面
     *
     * @param data     二维码内容
     * @param modeDesc 模式描述
     */
    void gotoShowQRCodeView(String data, String modeDesc);

    /**
     * 从充值页面返回后，刷新余额
     *
     * @param balance 余额
     */
    void refreshBalance(Double balance);
}
