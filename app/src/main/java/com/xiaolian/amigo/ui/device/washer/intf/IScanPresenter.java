package com.xiaolian.amigo.ui.device.washer.intf;

import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 扫描二维码
 *
 * @author zcd
 * @date 18/1/17
 */

public interface IScanPresenter<V extends IScanView> extends IBasePresenter<V> {
    /**
     * 将扫描到的二维码内容提交到服务器，进行预结账
     * @param content 二维码内容
     */
    void scanCheckout(String content , int type);

    /**
     * 检查设备
     * @param type
     */
    void checkDeviceUseage(int type , String mac ,boolean isBle);






    /**
     * 跳转到热水澡
     *
     * @param defaultMacAddress 默认mac地址
     * @param defaultSupplierId 默认供应商id
     * @param location          位置
     */
    void gotoHeaterDevice(String defaultMacAddress, Long defaultSupplierId,
                          String location ,long resdienceId);

    /**
     * 扫一扫获取设备信息
     * @param macAddress
     * @param isBle
     */
    void getDeviceDetail(boolean isTimeValid,int type,String macAddress , boolean isBle);


    User getUserInfo();

    String getAccessToken();

    String getRefreshToken();

    /**
     * 获取设备详细信息
     * @param unique 设备唯一标识
     */
    void getDeviceDetail(String unique);
}
