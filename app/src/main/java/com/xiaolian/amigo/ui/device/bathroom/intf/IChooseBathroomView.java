package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomOuterAdapter;

import java.util.List;

/**
 * @author zcd
 * @date 18/7/3
 */
public interface IChooseBathroomView extends IBaseView {
    void refreshBathroom(List<ChooseBathroomOuterAdapter.BathGroupWrapper> wrappers,
                         List<Integer> methods, Integer missTimes);

    void gotoBookingView(Double balance,
                         Long bonusId, String bonusDesc, Double bonusAmount,
                         Long expiredTime, String location, Integer maxMissAbleTimes,
                         Double minPrepay, Integer missedTimes, Double prepay , String reservedTime);


    void startPreconditionView(BathOrderPreconditionRespDTO respDTO);

    /**
     * 显示动画
     */
    void showBathroomDialog();

    /**
     * 隐藏动画
     */
    void hideBathroomDialog();

    /**
     * 设置标题名字
     * @param name
     */
    void setTvTitle(String name);

    /**
     * 设置button字体
     * @param text
     */
    void setBtnText(String text);
}
