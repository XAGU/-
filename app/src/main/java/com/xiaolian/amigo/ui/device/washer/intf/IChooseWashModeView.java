package com.xiaolian.amigo.ui.device.washer.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.device.washer.ChooseWashModeAdapter;

import java.util.List;

/**
 * 选择洗衣机模式
 * <p>
 * Created by zcd on 18/1/12.
 */

public interface IChooseWashModeView extends IBaseView {
    void addMore(List<ChooseWashModeAdapter.WashModeItem> items);

    void gotoShowQRCodeView(String data);
}
