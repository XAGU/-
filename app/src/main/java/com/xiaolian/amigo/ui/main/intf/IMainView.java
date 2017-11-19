package com.xiaolian.amigo.ui.main.intf;

import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.dto.response.BannerDTO;
import com.xiaolian.amigo.data.network.model.dto.response.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.dto.response.VersionDTO;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.main.update.IVersionModel;

import java.util.List;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public interface IMainView extends IBaseView {
    void showNoticeAmount(Integer amount);

    void showTimeValidDialog(int deviceType, DeviceCheckRespDTO data);

    void gotoDevice(Device device, String macAddress, String location, Long residenceId, boolean recovery);

    void showUrgentNotify(String content, Long id);

    void refreshNoticeAmount();

    void showBanners(List<BannerDTO> banners);

    void showSchoolBiz(List<BriefSchoolBusiness> businesses);

    void showDeviceUsageDialog(int type, DeviceCheckRespDTO data);

    void showBindDormitoryDialog();

    // 打开定位服务
    void showOpenLocationDialog();

    void showNoDeviceDialog();

    void refreshProfile(PersonalExtraInfoDTO data);

    void showUpdateDialog(IVersionModel version);
}
