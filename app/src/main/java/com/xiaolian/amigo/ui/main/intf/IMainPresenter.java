package com.xiaolian.amigo.ui.main.intf;

import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 主页
 *
 * @author zcd
 * @date 17/9/20
 */

public interface IMainPresenter<V extends IMainView> extends IBasePresenter<V> {

    /**
     * 获取accessToken
     * @return
     */
    String getAccessToken() ;

    /**
     * 获取refreshToken
     * @return
     */
    String getRefreshToken();
    /**
     * 是否登录
     *
     * @return 是否登录
     */
    boolean isLogin();

    /**
     * 退出登录
     */
    void logout();

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    User getUserInfo();

    long getSchoolId();

    /**
     * 获取通知个数
     */
    void getNoticeAmount();

    /**
     * 设置余额
     *
     * @param balance 余额
     */
    void setBalance(String balance);

    /**
     * 获取余额
     *
     * @return 余额
     */
    String getBalance();

    /**
     * 设置红包个数
     *
     * @param bonusAmount 红包个数
     */
    void setBonusAmount(int bonusAmount);

    /**
     * 获取红包个数
     *
     * @return 红包个数
     */
    int getBonusAmount();

    /**
     * 已读紧急通知
     *
     * @param id 通知id
     */
    void readUrgentNotify(Long id);

    /**
     * 是否显示紧急通知
     *
     * @return 是否显示紧急通知
     */
    boolean isShowUrgencyNotify();

    /**
     * 设置是否显示紧急通知
     *
     * @param isShow 是否显示紧急通知
     */
    void setShowUrgencyNotify(boolean isShow);

    /**
     * 跳转到热水澡
     *
     * @param defaultMacAddress 默认mac地址
     * @param defaultSupplierId 默认供应商id
     * @param location          位置
     * @param residenceId       宿舍id
     */
    void gotoHeaterDevice(String defaultMacAddress, Long defaultSupplierId,
                          String location, Long residenceId);

    /**
     * 获取学校业务
     */
    void getSchoolBusiness();

    /**
     * 设备使用前校验
     *
     * @param type 设备类型
     */
    void checkDeviceUsage(int type);

    /**
     * 检查更新
     *
     * @param code      versionCode
     * @param versionNo versionName
     */
    void checkUpdate(Integer code, String versionNo , String remindMobile);


    /**
     * 设置上次报修更改时间
     *
     * @param time 时间
     */
    void setLastRepairTime(Long time);


    /**
     * 上传设备信息
     *
     * @param appVersion    app版本
     * @param brand         手机品牌
     * @param model         手机型号
     * @param systemVersion 系统版本
     * @param androidId     androidId
     */
    void uploadDeviceInfo(String appVersion, String brand, String model, int systemVersion, String androidId);

    /**
     * 缓存积分
     *
     * @param credits 积分
     */
    void setCredits(Integer credits);

    /**
     * 获取缓存中的积分
     *
     * @return 积分
     */
    Integer getCredits();

    void deletePushToken();

    String getPushToken();

    void setPushToken(String pushToken);

    void routeHeaterOrBathroom();

    /**
     * 获取上一个订单状态
     */
    void currentOrder();


    void noticeCount();

    int getNoticeCount();

    boolean getCommentEnable();

    /**
     * 返回是否显示设备保修
     * @return
     */
    boolean getIsShowRepair();

    boolean getIsFirstAfterLogin();

    void setIsFirstAfterLogin(boolean b);

    void setCertifyStatus(int statusType);

    void deleteFile();

    /**
     * 获取滚动公告
     */
    void rollingNotify();

    /**
     * 获取首页学校论坛状态
     */
    void getSchoolForumStatus();

    String getRemindMobile();
}
