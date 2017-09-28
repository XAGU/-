package com.xiaolian.amigo.ui.widget.wheelpicker;

/**
 * 分钟选择器接口
 * <p>
 * Created by zcd on 9/28/17.
 */

public interface IWheelMinutePicker {
    /**
     * 获取选择器初始化时选择的分钟
     *
     * @return 选择的分钟
     */
    int getSelectedMinute();

    /**
     * 设置小时选择器初始化时选择的分钟
     *
     * @param minute 选择的分钟
     */
    void setSelectedMinute(int minute);

    /**
     * 获取当前选择的分钟
     *
     * @return 当前选择的分钟
     */
    int getCurrentMinute();
}
