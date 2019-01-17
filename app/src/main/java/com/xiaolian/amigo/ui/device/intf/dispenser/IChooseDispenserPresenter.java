package com.xiaolian.amigo.ui.device.intf.dispenser;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

import java.util.List;

/**
 * 选择饮水机
 *
 * @author zcd
 * @date 17/10/13
 */

public interface IChooseDispenserPresenter<V extends IChooseDispenerView> extends IBasePresenter<V> {
    /**
     * 获取已收藏的饮水机
     */
    void requestFavorites();

    /**
     * 页面加载时触发
     */
    void onLoad();

    /**
     * 关闭蓝牙连接
     */
    void closeBleConnection();

    /**
     * 设置列表状态
     * false 表示附近列表
     * true 表示收藏列表
     *
     * @param listStatus 列表状态
     */
    void setListStatus(boolean listStatus);

    /**
     * 设置action
     *
     * @param action action
     */
    void setAction(int action);

    /**
     * 获取action
     *
     * @return action
     */
    int getAction();

    /**
     * 结束页面
     */
    void finishView();

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

    /**
     * 开始计时
     */
    void startTimer();

    /**
     * 取消计时
     */
    void cancelTimer();

    /**
     * 设置设备类型
     *
     * @param deviceType 设备类型
     */
    void setDeviceType(Integer deviceType);

    /**
     * 跳转到饮水机页
     *
     * @param macAddress  设备mac地址
     * @param supplierId  供应商id
     * @param favor       是否已收藏
     * @param residenceId 位置id
     * @param usefor      水温
     * @param location    设备位置
     * @param preOrderCopy 下单前文案
     * @param afterOrderCopy 下单后文案
     */
    void gotoDispenser(String macAddress, Long supplierId, boolean favor,
                       Long residenceId, String usefor, String location ,
                       List<String> preOrderCopy , List<String> afterOrderCopy);

}
