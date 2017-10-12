package com.xiaolian.amigo.ui.main.intf;

import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

import java.util.List;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public interface IMainView extends IBaseView {
    void showNoticeAmount(Integer amount);

    void showTimeValidDialog(String title, String remark, Class clz, int deviceType);

    void gotoDevice(Class clz);

    void gotoDevice(Class clz, String macAddress, String location);

    void showUrgentNotify(String content, Long id);

    void refreshNoticeAmount();

    void showBanners(List<String> banners);

    void showSchoolBiz(List<BriefSchoolBusiness> businesses);

    void showPrepayOrder(List<Order> orders);
}
