package com.xiaolian.amigo.data.prefs;

import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.data.network.model.user.UploadUserDeviceInfoReqDTO;
import com.xiaolian.amigo.data.vo.DeviceCategory;
import com.xiaolian.amigo.data.vo.NormalBathroom;
import com.xiaolian.amigo.data.vo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * SharedPreference帮助接口
 *
 * @author zcd
 * @date 17/9/15
 */

public interface ISharedPreferencesHelp {
    // 储存蓝牙地址和编号的映射关系
    String getToken();

    void setToken(String token);

    User getUserInfo();

    void setUserInfo(User user);

    boolean isShowUrgencyNotify();

    void setShowUrgencyNotify(boolean isShow);

    void setBonusAmount(int amount);

    int getBonusAmount();

    void setBalance(String balance);

    String getBalance();

    void logout();

    // current http device token
    void setCurrentDeviceToken(String deviceToken);

    String getCurrentDeviceToken();

    // http device token
    void setDeviceToken(String macAddress, String deviceToken);

    String getDeviceToken(String macAddress);

    void setDeviceOrderId(String macAddress, Long orderId);

    Long getDeviceOrderId(String macAddress);

    // 握手指令
    void setConnectCmd(String macAddress, String connectCmd);

    String getConnectCmd(String macAddress);

    // 关阀指令
    void setCloseCmd(String macAddress, String closeCmd);

    String getCloseCmd(String macAddress);

    void setDeviceNoAndMacAddress(String deviceNo, String macAddress);

    String getMacAddressByDeviceNo(String deviceNo);

    // 上次连接时间
    void setLastConnectTime(Long lastConnectTime);

    Long getLastConnectTime();

    // 更新提示
    Long getLastUpdateRemindTime();

    void setLastUpdateRemindTime();

    // 上次选择的充值id
    void setLastWithdrawId(Long id);

    Long getLastWithdrawId();

    // 上次选择的提现账户
    void setLastWithdrawName(String name);

    String getLastWithdrawName();

    // 上次选择的提现金额
    String getLastRechargeAmount();

    void setLastRechargeAmount(String amount);

    // 引导页相关
    void setMainGuide(Integer guideTime);

    Integer getMainGuide();

    // 显示温馨提示
    void setHeaterGuide(Integer guideTime);

    Integer getHeaterGuide();

    void setDispenserGuide(Integer guideTime);

    Integer getDispenserGuide();

    Integer getDryerGuide();

    void setDryerGuide(Integer guideTime);

    void setLastRepairTime(Long time);

    void setRepairGuide(Integer guideTime);

    Integer getRepairGuide();

        Long getLastRepairTime();

    void setRememberMobile(String mobile);

    String getRememberMobile();

    void setDeviceResult(String deviceNo, String result);

    String getDeviceResult(String deviceNo);

    void saveUploadedUserDeviceInfo(UploadUserDeviceInfoReqDTO reqDTO);

    UploadUserDeviceInfoReqDTO getUploadedUserDeviceInfo();

    // 存储设备信息
    void saveDeviceCategory(List<DeviceCategory> devices);

    List<DeviceCategory> getDeviceCategory();

    void setTransfer(boolean b);

    boolean getTransfer();

    /**
     * 缓存积分
     * @param credits 积分
     */
    void setCredits(Integer credits);
    Integer getCredits();

    // 缓存学校业务
    void setSchoolBiz(List<BriefSchoolBusiness> businesses);

    List<BriefSchoolBusiness> getSchoolBiz();

    void saveScanType(int scanType);

    int getScanType();

    void setPushToken(String pushToken);

    String getPushToken();

    void setPushTag(String pushTag);

    String getPushTag();

    void setBathPasswordDescription(ArrayList<String> bathPasswordDescription);

    List<String> getBathPasswordDescription();

    /**
     * 缓存默认浴室地址
     * @param normalBathroom
     */
    void setNormalBathroom(NormalBathroom normalBathroom);

    /**
     * 得到默认浴室地址
     * @return
     */
    NormalBathroom getNormalBathroom();

    void setBathroomPassword(String password);

    String getBathroomPassword();
}
