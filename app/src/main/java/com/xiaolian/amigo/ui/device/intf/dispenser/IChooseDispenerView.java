package com.xiaolian.amigo.ui.device.intf.dispenser;

import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;
import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.device.dispenser.ChooseDispenserAdaptor;

import java.util.List;

/**
 * 选择饮水机
 * <p>
 * Created by zcd on 10/13/17.
 */

public interface IChooseDispenerView extends IBaseListView {
    void addMore(List<ChooseDispenserAdaptor.DispenserWapper> wrappers);

    // 添加扫描到的设备
    void addScanDevices(List<ScanDeviceGroup> devices);
}
