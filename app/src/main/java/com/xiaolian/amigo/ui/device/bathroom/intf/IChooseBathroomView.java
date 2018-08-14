package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.data.network.model.bathroom.BathOrderCurrentRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BuildingTrafficDTO;
import com.xiaolian.amigo.data.network.model.bathroom.QueueInfo;
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
    void setBtnText(String text , boolean isSelected);


    /**
     * 现在楼层浴室洗澡信息
     */
    void trafficInfo(List<BuildingTrafficDTO.FloorsBean> floorsBeans);

    /**
     * 去排队状态界面
     * @param
     */
    void startQueue(long id );

    /**
     * 去预约状态界面
     * @param bathOrderId
     */
    void startBooking(long bathOrderId);

    /**
     * 保存预约信息，包括最大失约次数， 已失约次数，预付信息
     * @param data
     */
    void saveBookingInfo(BathOrderPreconditionRespDTO data);

    /**
     * 去使用中界面
     * @param bathOrderId
     */
    void startUsing(long bathOrderId);

    /**
     * 去结算页面
     */
    void startOrderInfo(BathOrderCurrentRespDTO data);
}
