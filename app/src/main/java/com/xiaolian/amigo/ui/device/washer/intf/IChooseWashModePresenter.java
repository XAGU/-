package com.xiaolian.amigo.ui.device.washer.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 选择洗衣机模式
 *
 * @author zcd
 * @date 18/1/12
 */

public interface IChooseWashModePresenter<V extends IChooseWashModeView> extends IBasePresenter<V> {

    /**
     * 获取device编号
     *
     * @return device编号
     */
    String getDeviceNo();

    /**
     * 设置device编号
     *
     * @param deviceNo device编号
     */
    void setDeviceNo(String deviceNo);

    /**
     * 获取洗衣模式
     */
    void getWasherMode();

    /**
     * 支付并获取二维码内容
     *
     * @param bonusId  红包id
     * @param modeDesc 模式描述
     * @param price    实际价格减去红包金额后的价格
     * @param mode     模式id
     */
    void payAndGenerate(Long bonusId, String modeDesc, Double price, Integer mode);

    /**
     * 获取余额
     */
    void getBalance();

    /**
     * 获取缓存中的余额
     *
     * @return 余额
     */
    Double getLocalBalance();
}
