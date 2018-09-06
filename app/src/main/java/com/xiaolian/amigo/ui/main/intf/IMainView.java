package com.xiaolian.amigo.ui.main.intf;

import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.bathroom.BathRouteRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.CurrentBathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.main.update.IVersionModel;

import java.util.List;

/**
 * 主页
 *
 * @author zcd
 * @date 17/9/20
 */

public interface IMainView extends IBaseView {
    /**
     * 显示通知个数
     *
     * @param amount 通知个数
     */
    void showNoticeAmount(Integer amount);

    /**
     * 显示是否是有效时间对话框
     *
     * @param deviceType 设备类型
     * @param data       设备校验结果
     */
    void showTimeValidDialog(int deviceType, DeviceCheckRespDTO data);

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
                    String location, Long residenceId, boolean recovery);

    /**
     * 显示紧急通知
     *
     * @param content 内容
     * @param id      通知id
     */
    void showUrgentNotify(String content, Long id);

    /**
     * 刷新通知个数
     */
    void refreshNoticeAmount();

    /**
     * 显示banner
     *
     * @param banners banner
     */
    void showBanners(List<BannerDTO> banners);

    /**
     * 显示学校业务
     *
     * @param businesses 学校业务
     */
    void showSchoolBiz(List<BriefSchoolBusiness> businesses);

    /**
     * 显示设备使用状态对话框
     *
     * @param type 设备类型
     * @param data 设备校验结果
     */
    void showDeviceUsageDialog(int type, DeviceCheckRespDTO data);

//    /**
//     * 显示绑定宿舍对话框
//     */
//    void showBindDormitoryDialog();

    /**
     * 打开定位服务
     */
    void showOpenLocationDialog();

    /**
     * 显示没有设备对话框
     */
    void showNoDeviceDialog();

    /**
     * 刷新个人信息
     *
     * @param data 个人额外信息
     */
    void refreshProfile(PersonalExtraInfoDTO data);

    /**
     * 显示更新对话框
     *
     * @param version 版本数据
     */
    void showUpdateDialog(IVersionModel version);

    /**
     * 获取androidId
     *
     * @return androidId
     */
    String getAndroidId();

    /**
     * 获取app版本
     *
     * @return app版本
     */
    String getAppVersion();

    /**
     * 显示校Ok账号迁移
     */
    void showXOkMigrate();

    /**
     * 隐藏校Ok帐号迁移
     */
    void hideXOkMigrate();

    /**
     * 路由到宿舍
     */
    void routeToRoomShower(BathRouteRespDTO dto);

    /**
     * 路由到公共浴室
     */
    void routeToBathroomShower(BathRouteRespDTO dto);


    /**
     * 洗澡地址为空时,选择洗澡地址
     */
    void choseBathroomAddress();

    /**
     * 洗澡地址为空时，跳转到配置用户性别、宿舍洗澡地址信息页面
     */
    void gotoCompleteInfoActivity(BathRouteRespDTO dto);


    /**
     * 显示上一洗澡订单状态
     */
    void currentOrder(CurrentBathOrderRespDTO dto);


    /**
     * 测试,直接写死数据
     */
    void startToBathroomShower();
}
