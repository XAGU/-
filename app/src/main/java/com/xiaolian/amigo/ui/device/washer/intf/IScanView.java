package com.xiaolian.amigo.ui.device.washer.intf;

import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.device.BriefDeviceDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
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


    /**
     * 显示设备使用状态对话框
     *
     * @param type 设备类型
     * @param data 设备校验结果
     */
    void showDeviceUsageDialog(int type, DeviceCheckRespDTO data , String mac ,boolean isBle);

    /**
     * 跳转到设备页面
     *
     * @param device      设备类型
     * @param macAddress  mac地址
     * @param supplierId  供应商id
     * @param location    位置
     * @param residenceId 位置id
     * @param recovery    是否显示正在恢复
     */
    void gotoDevice(Device device, String macAddress, Long supplierId,
                    long residenceId,String location, boolean recovery);


    /**
     * 显示绑定宿舍对话框
     */
    void showBindDormitoryDialog();

    /**
     * 显示是否是有效时间对话框
     *
     * @param deviceType 设备类型
     * @param data       设备校验结果
     */
    void showTimeValidDialog(int deviceType, DeviceCheckRespDTO data);


    /**
     * 显示没有设备对话框
     */
    void showNoDeviceDialog();


    /**
     * 显示扫描设备时， 返现存在2小时内未结账订单
     * @param type
     * @param dto
     * @param orderPreInfoDTO
     */
    void showScanDialog(int type , DeviceCheckRespDTO dto , OrderPreInfoDTO orderPreInfoDTO);

}
