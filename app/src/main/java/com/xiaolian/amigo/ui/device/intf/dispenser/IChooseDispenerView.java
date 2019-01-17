package com.xiaolian.amigo.ui.device.intf.dispenser;

import com.xiaolian.amigo.data.vo.ScanDeviceGroup;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.device.dispenser.ChooseDispenserAdaptor;

import java.util.List;

/**
 * 选择饮水机
 *
 * @author zcd
 * @date 17/10/13
 */

public interface IChooseDispenerView extends IBaseView {
    /**
     * 刷新选择饮水机列表
     *
     * @param wrappers 列表元素
     */
    void addMore(List<ChooseDispenserAdaptor.DispenserWrapper> wrappers);

    /**
     * 添加扫描到的设备
     *
     * @param devices 扫描到的设备
     */
    void addScanDevices(List<ScanDeviceGroup> devices);

    /**
     * 显示空列表
     */
    void showEmptyView();

    /**
     * 隐藏空列表
     */
    void hideEmptyView();

    /**
     * 显示错误页面
     */
    void showErrorView();

    /**
     * 隐藏错误页面
     */
    void hideErrorView();

    /**
     * 停止刷新
     */
    void completeRefresh();

    /**
     * 结束页面
     */
    void finishView();

    /**
     * 跳转到饮水机页面
     *
     * @param macAddress  设备mac地址
     * @param supplierId  设备供应商id
     * @param favor       是否已收藏
     * @param residenceId 位置id
     * @param usefor      水温
     * @param location    设备位置
     * @param preOrderCopy 下单前文案
     * @param afterOrderCopy 下单后文案
     */
    void gotoDispenser(String macAddress, Long supplierId,
                       boolean favor, Long residenceId,
                       String usefor, String location ,
                        List<String> preOrderCopy , List<String> afterOrderCopy);

    /**
     * 显示扫描结束
     */
    void showScanStopView();

    /**
     * 跳转到吹风机页面
     *
     * @param deviceNo    设备编号
     * @param supplierId  供应商id
     * @param isFavor     是否已收藏
     * @param residenceId 位置id
     * @param location    设备位置
     */
    void gotoDryer(String deviceNo, Long supplierId, Boolean isFavor, Long residenceId, String location);
}
