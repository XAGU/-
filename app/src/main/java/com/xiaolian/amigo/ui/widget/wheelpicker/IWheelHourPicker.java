package com.xiaolian.amigo.ui.widget.wheelpicker;

/**
 * 小时选择器接口
 *
 * @author zcd
 * @date 17/9/28
 */

public interface IWheelHourPicker {
    /**
     * 获取选择器初始化时选择的小时
     *
     * @return 选择的小时
     */
    int getSelectedHour();

    /**
     * 设置小时选择器初始化时选择的小时
     *
     * @param hour 选择的小时
     */
    void setSelectedHour(int hour);

    /**
     * 获取当前选择的小时
     *
     * @return 当前选择的小时
     */
    int getCurrentHour();
}
