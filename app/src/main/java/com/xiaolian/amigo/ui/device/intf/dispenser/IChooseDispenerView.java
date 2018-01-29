package com.xiaolian.amigo.ui.device.intf.dispenser;

import com.xiaolian.amigo.data.vo.ScanDeviceGroup;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.device.dispenser.ChooseDispenserAdaptor;

import java.util.List;

/**
 * 选择饮水机
 * <p>
 * Created by zcd on 10/13/17.
 */

public interface IChooseDispenerView extends IBaseView {
    void addMore(List<ChooseDispenserAdaptor.DispenserWrapper> wrappers);

    // 添加扫描到的设备
    void addScanDevices(List<ScanDeviceGroup> devices);

    void showEmptyView();

    void hideEmptyView();

    void showErrorView();

    void hideErrorView();

    void completeRefresh();

    void finishView();

    void gotoDispenser(String macAddress, Long supplierId,
                       boolean favor, Long residenceId,
                       String usefor, String location);

    void showScanStopView();

    void gotoDryer(String deviceNo, Long supplierId, Boolean isFavor, Long residenceId, String location);
}
