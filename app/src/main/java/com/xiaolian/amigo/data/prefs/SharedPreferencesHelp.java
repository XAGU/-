package com.xiaolian.amigo.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaolian.amigo.data.network.model.lostandfound.BbsTopicListTradeRespDTO;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.data.network.model.user.UploadUserDeviceInfoReqDTO;
import com.xiaolian.amigo.data.vo.DeviceCategory;
import com.xiaolian.amigo.data.vo.NormalBathroom;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.di.ApplicationContext;
import com.xiaolian.blelib.BluetoothConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * SharedPreferencesHelp实现类
 *
 * @author zcd
 * @date 17/9/15
 */
public class    SharedPreferencesHelp implements ISharedPreferencesHelp {
    private static final String PREF_FILE_NAME = "amigo";
    private static final String PREF_KEY_TOKEN = "PREF_KEY_TOKEN";
    private static final String PREF_DEVICE_TOKEN_PREFIX = "PREF_DEVICE_TOKEN_";
    private static final String PREF_DEVICE_RESULT_PREFIX = "PREF_DEVICE_RESULT_PREFIX";
    private static final String PREF_CURRENT_DEVICE_TOKEN = "PREF_CURRENT_DEVICE_TOKEN";
    private static final String PREF_KEY_RESIDENCE_ID = "PREF_KEY_RESIDENCE_ID";
    private static final String PREF_KEY_RESIDENCE_NAME = "PREF_KEY_RESIDENCE_NAME";
    private static final String PREF_KEY_MAC_ADDRESS = "PREF_KEY_MAC_ADDRESS";
    private static final String PREF_KEY_SCHOOL_ID = "PREF_KEY_SCHOOL_ID";
    private static final String PREF_KEY_SCHOOL_NAME = "PREF_KEY_SCHOOL_NAME";
    private static final String PREF_KEY_NICKNAME = "PREF_KEY_NICKNAME";
    private static final String PREF_KEY_UID = "PREF_KEY_UID";
    private static final String PREF_KEY_MOBILE = "PREF_KEY_MOBILE";
    private static final String PREF_KEY_PICTURE_URL = "PREF_KEY_PICTURE_URL";
    private static final String PREF_KEY_BUILD_ID = "PREF_KEY_BUILD_ID";
    private static final String PREF_KEY_SEX = "PREF_KEY_SEX" ;
    private static final String PREF_KEY_ROOMID = "PREF_KEY_ROOMID" ;
    private static final String PREF_CMD_CONNECT_PREFIX = "PREF_CMD_CONNECT_";
    private static final String PREF_CMD_CLOSE_PREFIX = "PREF_CMD_CLOSE_";
    private static final String PREF_ORDER_ID_PREFIX = "PREF_ORDER_ID_PREFIX";
    private static final String PREF_RESULT_DEVICE = "PREF_RESULT_DEVICE";
    private static final String PREF_LAST_CONNECT_TIME = "PREF_LAST_CONNECT_TIME";
    private static final String PREF_KEY_BALANCE = "PREF_KEY_BALANCE";
    private static final String PREF_KEY_BONUS = "PREF_KEY_BONUS";
    private static final String PREF_BLUETOOTH_MAC_ADDRESS_PREFIX = "PREF_BLUETOOTH_MAC_ADDRESS_PREFIX";
    private static final String PREF_LAST_UPDATE_REMIND_TIME = "PREF_LAST_UPDATE_REMIND_TIME";
    private static final String PREF_LAST_VIEW_REPAIR = "PREF_LAST_VIEW_REPAIR";
    private static final String PREF_LAST_VIEW_REPAIR_PREFIX = "PREF_LAST_VIEW_REPAIR_PREFIX";
    private static final String PREF_LAST_WITHDRAW_ID = "PREF_LAST_WITHDRAW_ID";
    private static final String PREF_LAST_WITHDRAW_NAME = "PREF_LAST_WITHDRAW_NAME";
    private static final String PREF_LAST_RECHARGE_AMOUNT = "PREF_LAST_RECHARGE_AMOUNT";
    private static final String PREF_UPLOADED_USER_DEVICE_INFO = "PREF_UPLOADED_USER_DEVICE_INFO";
    private static final String PREF_KEY_DEVICE_CATEGORY = "PREF_KEY_DEVICE_CATEGORY";
    private static final String PREF_KEY_SCHOOL_BIZ = "PREF_KEY_SCHOOL_BIZ";
    private static final String PREF_KEY_USER_CREATE_TIME = "PREF_KEY_USER_CREATE_TIME";
    private static final String PREF_KEY_PUSH_TAG = "PREF_KEY_PUSH_TAG";
    /**
     * 积分
     */
    private static final String PREF_KEY_CREDITS = "PREF_KEY_CREDITS";
    /************* 引导页相关 *******************/
    private static final String PREF_GUIDE_NAME = "PREF_GUIDE_NAME";
    private static final String PREF_GUIDE_MAIN = "PREF_GUIDE_MAIN";
    private static final String PREF_ALERT_HEATER = "PREF_ALERT_HEATER";
    private static final String PREF_ALERT_DISPENSER = "PREF_ALERT_DISPENSER";
    private static final String PREF_ALERT_DRYER = "PREF_ALERT_DRYER";
    private static final String PREF_ALERT_REPAIR = "PREF_ALERT_REPAIR";
    /************* 记住手机号 ******************/
    private static final String PREF_REMEMBER_MOBILE = "PREF_REMEMBER_MOBILE";
    /**
     * 扫描方式
     */
    private static final String PREF_KEY_SCAN_TYPE = "PREF_KEY_SCAN_TYPE";

    /**
     * 浴室密码说明
     */
    private static final String PREF_KEY_BATH_ROOM_PASSWORD_DESC = "PREF_KEY_BATH_ROOM_PASSWORD_DESC";


    /**
     * 浴室密码
     */
    private static final String PREF_KEY_BATH_ROOM_PASSWORD = "PREF_KEY_BATH_ROOM_PASSWORD";

    private static final String PREF_KEY_BOOK_METHOD ="PREF_KEY_BOOK_METHOD";


    /**
     * 社交标签
     */
    private static final String PREF_KEY_SOCAL_TAG = "PREF_KEY_SOCAL_TAG";

    private static final String PREF_KEY_COMMENTABLE = "PREF_KEY_COMMENTABLE";

    private static final String PREF_KEY_ISFIRST_AFTER_LOGIN = "PREF_KEY_ISFIRST_AFTER_LOGIN";


    /**
     * 身份认证
     */
    private static final String PREF_KEY_DEPARTMENT = "PREF_KEY_DEPARTMENT";  //院系

    private static final String PREF_KEY_PROFESSION = "PREF_KEY_PROFESSION";  //专业

    private static final String PREF_KEY_GRADE = "PREF_KEY_GRADE";  //年级

    private static final String PREF_KEY_CALSS = "PREF_KEY_CLASS";  //班级

    private static final String PREF_KEY_STUDENT_ID = "PREF_KEY_STUDENT_ID"; // 学号

    private static final String PREF_KEY_DORMITORY = "PREF_KEY_DORMITORY";  //宿舍


    private static final String PREF_KEY_CERTIFY_STATUS = "PREF_KEY_CERTIFY_STATUS";


    /**
     * 是否需要弹出退费说明
     */
    private static final String PREF_KEY_WITHDRAW = "PREF_KEY_WITHDRAW";

    private String tokenHolder;
    private String deviceTokenHolder;
    private User userHolder;

    private boolean showUrgencyNotify = true;
    private int bonusAmount = 0;
    private String balance = "";

    private final SharedPreferences mSharedPreferences;
    private final SharedPreferences mUnclearSharedPreferences;
    private final Gson mGson;
    // 是否需要账户迁移
    private boolean transfer;
    // 推送token
    private String pushToken;




    @Inject
    public SharedPreferencesHelp(@ApplicationContext Context context, Gson gson) {
        mSharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        mUnclearSharedPreferences = context.getSharedPreferences(PREF_GUIDE_NAME, Context.MODE_PRIVATE);
        mGson = gson;
    }

    @Override
    public String getToken() {
        if (tokenHolder != null) {
            return tokenHolder;
        }
        tokenHolder = mSharedPreferences.getString(PREF_KEY_TOKEN, null);
        return tokenHolder;
    }

    @Override
    public void setToken(String token) {
        tokenHolder = token;
        mSharedPreferences.edit().putString(PREF_KEY_TOKEN, token).commit();
    }

    @Override
    public void setCurrentDeviceToken(String deviceToken) {
        mSharedPreferences.edit().putString(PREF_CURRENT_DEVICE_TOKEN, deviceToken).commit();
    }

    @Override
    public String getCurrentDeviceToken() {
        return mSharedPreferences.getString(PREF_CURRENT_DEVICE_TOKEN, null);
    }

    @Override
    public void setDeviceToken(String macAddress, String deviceToken) {
        mSharedPreferences.edit().putString(PREF_DEVICE_TOKEN_PREFIX + macAddress, deviceToken).commit();
    }

    @Override
    public String getDeviceToken(String macAddress) {
        return mSharedPreferences.getString(PREF_DEVICE_TOKEN_PREFIX + macAddress, null);
    }

    @Override
    public void setDeviceOrderId(String macAddress, Long orderId) {
        mSharedPreferences.edit().putLong(PREF_ORDER_ID_PREFIX + macAddress, orderId).apply();
    }

    @Override
    public Long getDeviceOrderId(String macAddress) {
        return mSharedPreferences.getLong(PREF_ORDER_ID_PREFIX + macAddress, -1);
    }

    @Override
    public User getUserInfo() {
        if (!isUserHolderEmpty()) {
            return userHolder;
        }
        if (userHolder == null) {
            userHolder = new User();
        }
        userHolder.setId(mSharedPreferences.getLong(PREF_KEY_UID, -1));
        userHolder.setResidenceId(mSharedPreferences.getLong(PREF_KEY_RESIDENCE_ID, -1L));
        userHolder.setResidenceName(mSharedPreferences.getString(PREF_KEY_RESIDENCE_NAME, ""));
        userHolder.setMacAddress(mSharedPreferences.getString(PREF_KEY_MAC_ADDRESS, null));
        userHolder.setSchoolId(mSharedPreferences.getLong(PREF_KEY_SCHOOL_ID, -1L));
        userHolder.setSchoolName(mSharedPreferences.getString(PREF_KEY_SCHOOL_NAME, null));
        userHolder.setNickName(mSharedPreferences.getString(PREF_KEY_NICKNAME, null));
        userHolder.setMobile(mSharedPreferences.getString(PREF_KEY_MOBILE, null));
        userHolder.setPictureUrl(mSharedPreferences.getString(PREF_KEY_PICTURE_URL, null));
        userHolder.setCreateTime(mSharedPreferences.getLong(PREF_KEY_USER_CREATE_TIME, 0L));
        userHolder.setBuildingId(mSharedPreferences.getLong(PREF_KEY_BUILD_ID, -1L));
        userHolder.setSex(mSharedPreferences.getInt(PREF_KEY_SEX , -1));
        userHolder.setRoomId(mSharedPreferences.getLong(PREF_KEY_ROOMID ,-1l));
        userHolder.setDepartment(mSharedPreferences.getString(PREF_KEY_DEPARTMENT ,""));
        userHolder.setProfession(mSharedPreferences.getString(PREF_KEY_PROFESSION , ""));
        userHolder.setGrade(mSharedPreferences.getString(PREF_KEY_GRADE , ""));
        userHolder.setCalsses(mSharedPreferences.getString(PREF_KEY_CALSS , ""));
        userHolder.setStudentId(mSharedPreferences.getString(PREF_KEY_STUDENT_ID , ""));
        userHolder.setDormitory(mSharedPreferences.getString(PREF_KEY_DORMITORY , ""));
        return userHolder;
    }

    @Override
    public void setUserInfo(User user) {
        userHolder = user;
        if (null != user.getResidenceId()) {
            mSharedPreferences.edit().putLong(PREF_KEY_RESIDENCE_ID, user.getResidenceId()).apply();
        } else {
            mSharedPreferences.edit().putLong(PREF_KEY_RESIDENCE_ID, -1).apply();
        }
        if (null != user.getResidenceName()) {
            mSharedPreferences.edit().putString(PREF_KEY_RESIDENCE_NAME, user.getResidenceName()).apply();
        } else {
            mSharedPreferences.edit().putString(PREF_KEY_RESIDENCE_NAME, "").apply();
        }
        if (null != user.getMacAddress()) {
            mSharedPreferences.edit().putString(PREF_KEY_MAC_ADDRESS, user.getMacAddress()).apply();
        }
        if (null != user.getId()) {
            mSharedPreferences.edit().putLong(PREF_KEY_UID, user.getId()).apply();
        }
        if (null != user.getSchoolId()) {
            mSharedPreferences.edit().putLong(PREF_KEY_SCHOOL_ID, user.getSchoolId()).apply();
        }
        if (null != user.getSchoolName()) {
            mSharedPreferences.edit().putString(PREF_KEY_SCHOOL_NAME, user.getSchoolName()).apply();
        }
        if (null != user.getNickName()) {
            mSharedPreferences.edit().putString(PREF_KEY_NICKNAME, user.getNickName()).apply();
        }
        if (null != user.getMobile()) {
            mSharedPreferences.edit().putString(PREF_KEY_MOBILE, user.getMobile()).apply();
        }
        if (null != user.getPictureUrl()) {
            mSharedPreferences.edit().putString(PREF_KEY_PICTURE_URL, user.getPictureUrl()).apply();
        }
        if (null != user.getCreateTime()) {
            mSharedPreferences.edit().putLong(PREF_KEY_USER_CREATE_TIME,
                    user.getCreateTime()).apply();
        }
        if (null != user.getBuildingId()) {
            mSharedPreferences.edit().putLong(PREF_KEY_BUILD_ID,
                    user.getBuildingId()).apply();
        }

        if (null != user.getSex()){
            mSharedPreferences.edit().putInt(PREF_KEY_SEX,
                    user.getSex()).apply();
        }

        if (!TextUtils.isEmpty(user.getDepartment())) {
            mSharedPreferences.edit().putString(PREF_KEY_DEPARTMENT, user.getDepartment()).apply();
        }

        if (!TextUtils.isEmpty(user.getProfession())) {
            mSharedPreferences.edit().putString(PREF_KEY_PROFESSION, user.getProfession()).apply();
        }

        if (!TextUtils.isEmpty(user.getGrade())) {
            mSharedPreferences.edit().putString(PREF_KEY_GRADE, user.getGrade()).apply();
        }

        if (!TextUtils.isEmpty(user.getCalsses())) {
            mSharedPreferences.edit().putString(PREF_KEY_CALSS, user.getCalsses()).apply();
        }

        if (!TextUtils.isEmpty(user.getStudentId())) {
            mSharedPreferences.edit().putString(PREF_KEY_STUDENT_ID, user.getStudentId()).apply();
        }

        if (!TextUtils.isEmpty(user.getDormitory())) {
            mSharedPreferences.edit().putString(PREF_KEY_DORMITORY, user.getDormitory()).apply();
        }

    }




    @Override
    public boolean isShowUrgencyNotify() {
        return this.showUrgencyNotify;
    }

    @Override
    public void setShowUrgencyNotify(boolean isShow) {
        this.showUrgencyNotify = isShow;
    }

    @Override
    public void setBonusAmount(int amount) {
        this.bonusAmount = amount;
        mSharedPreferences.edit().putInt(PREF_KEY_BONUS, amount).apply();
    }

    @Override
    public int getBonusAmount() {
        if (bonusAmount == 0) {
            bonusAmount = mSharedPreferences.getInt(PREF_KEY_BONUS, 0);
        }
        return this.bonusAmount;
    }

    @Override
    public void setBalance(String balance) {
        this.balance = balance;
        mSharedPreferences.edit().putString(PREF_KEY_BALANCE, balance).apply();
    }

    @Override
    public String getBalance() {
        if (TextUtils.isEmpty(balance)) {
            balance = mSharedPreferences.getString(PREF_KEY_BALANCE, "");
        }
        return this.balance;
    }

    @Override
    public void setLastConnectTime(Long lastConnectTime) {
        mSharedPreferences.edit().putLong(PREF_LAST_CONNECT_TIME, System.currentTimeMillis()).apply();
    }

    @Override
    public Long getLastConnectTime() {
        return mSharedPreferences.getLong(PREF_LAST_CONNECT_TIME, 0L);
    }

    @Override
    public Long getLastUpdateRemindTime() {
        return mSharedPreferences.getLong(PREF_LAST_UPDATE_REMIND_TIME, 0L);
    }

    @Override
    public void setLastUpdateRemindTime() {
        mSharedPreferences.edit().putLong(PREF_LAST_UPDATE_REMIND_TIME, System.currentTimeMillis()).apply();
    }

    @Override
    public void setLastWithdrawId(Long id) {
        mSharedPreferences.edit().putLong(PREF_LAST_WITHDRAW_ID, id).apply();
    }

    @Override
    public Long getLastWithdrawId() {
        return mSharedPreferences.getLong(PREF_LAST_WITHDRAW_ID, -1);
    }

    @Override
    public void setLastWithdrawName(String name) {
        mSharedPreferences.edit().putString(PREF_LAST_WITHDRAW_NAME, name).apply();
    }

    @Override
    public String getLastWithdrawName() {
        return mSharedPreferences.getString(PREF_LAST_WITHDRAW_NAME, "");
    }

    @Override
    public String getLastRechargeAmount() {
        return mSharedPreferences.getString(PREF_LAST_RECHARGE_AMOUNT, "");
    }

    @Override
    public void setLastRechargeAmount(String amount) {
        mSharedPreferences.edit().putString(PREF_LAST_RECHARGE_AMOUNT, amount).apply();
    }

    @Override
    public void setMainGuide(Integer guideTime) {
        mUnclearSharedPreferences.edit().putInt(PREF_GUIDE_MAIN, guideTime).apply();
    }

    @Override
    public Integer getMainGuide() {
        return mUnclearSharedPreferences.getInt(PREF_GUIDE_MAIN, 0);
    }

    @Override
    public void setHeaterGuide(Integer guideTime) {
        mUnclearSharedPreferences.edit().putInt(PREF_ALERT_HEATER, guideTime).apply();
    }

    @Override
    public Integer getHeaterGuide() {
        return mUnclearSharedPreferences.getInt(PREF_ALERT_HEATER, 0);
    }

    @Override
    public void setDispenserGuide(Integer guideTime) {
        mUnclearSharedPreferences.edit().putInt(PREF_ALERT_DISPENSER, guideTime).apply();
    }

    @Override
    public Integer getDispenserGuide() {
        return mUnclearSharedPreferences.getInt(PREF_ALERT_DISPENSER, 0);
    }

    @Override
    public Integer getDryerGuide() {
        return mUnclearSharedPreferences.getInt(PREF_ALERT_DRYER, 0);
    }

    @Override
    public void setDryerGuide(Integer guideTime) {
        mUnclearSharedPreferences.edit().putInt(PREF_ALERT_DRYER, guideTime).apply();
    }

    @Override
    public void setLastRepairTime(Long time) {
        Long id = getUserInfo().getId();
        mUnclearSharedPreferences.edit().putLong(PREF_LAST_VIEW_REPAIR_PREFIX + id, time).apply();
    }

    @Override
    public void setRepairGuide(Integer guideTime) {
        mUnclearSharedPreferences.edit().putInt(PREF_ALERT_REPAIR, guideTime).apply();
    }

    @Override
    public Integer getRepairGuide() {
        return mUnclearSharedPreferences.getInt(PREF_ALERT_REPAIR, 0);
    }

    @Override
    public Long getLastRepairTime() {
        Long id = getUserInfo().getId();
        return mUnclearSharedPreferences.getLong(PREF_LAST_VIEW_REPAIR_PREFIX + id, 0);
    }

    @Override
    public void setRememberMobile(String mobile) {
        mUnclearSharedPreferences.edit().putString(PREF_REMEMBER_MOBILE, mobile).apply();
    }

    @Override
    public String getRememberMobile() {
        return mUnclearSharedPreferences.getString(PREF_REMEMBER_MOBILE, "");
    }

    @Override
    public void setDeviceResult(String deviceNo, String result) {
        mSharedPreferences.edit().putString(PREF_DEVICE_RESULT_PREFIX + deviceNo, result).commit();
    }

    @Override
    public String getDeviceResult(String deviceNo) {
        return mSharedPreferences.getString(PREF_DEVICE_RESULT_PREFIX + deviceNo, "");
    }

    @Override
    public void saveUploadedUserDeviceInfo(UploadUserDeviceInfoReqDTO reqDTO) {
        String deviceInfoStr = mGson.toJson(reqDTO);
        mSharedPreferences.edit()
                .putString(PREF_UPLOADED_USER_DEVICE_INFO, deviceInfoStr)
                .apply();
    }

    @Override
    public UploadUserDeviceInfoReqDTO getUploadedUserDeviceInfo() {
        String deviceInfoStr = mSharedPreferences.getString(PREF_UPLOADED_USER_DEVICE_INFO, null);
        if (null != deviceInfoStr) {
            return mGson.fromJson(deviceInfoStr, UploadUserDeviceInfoReqDTO.class);
        }
        return null;
    }

    @Override
    public void saveDeviceCategory(List<DeviceCategory> devices) {
        String deviceCategoryStr = mGson.toJson(devices);
        mSharedPreferences
                .edit()
                .putString(PREF_KEY_DEVICE_CATEGORY, deviceCategoryStr)
                .apply();
    }

    @Override
    public List<DeviceCategory> getDeviceCategory() {
        String deviceCategoryStr = mSharedPreferences.getString(PREF_KEY_DEVICE_CATEGORY, null);
        if (null != deviceCategoryStr) {
            return mGson.fromJson(deviceCategoryStr,
                    new TypeToken<List<DeviceCategory>>() {
                    }.getType());
        }
        return Collections.emptyList();
    }

    public void setTransfer(boolean b) {
        transfer = b;
    }

    @Override
    public boolean getTransfer() {
        return transfer;
    }

    @Override
    public void setCredits(Integer credits) {
        mSharedPreferences
                .edit()
                .putInt(PREF_KEY_CREDITS, credits)
                .commit();
    }

    @Override
    public Integer getCredits() {
        return mSharedPreferences.getInt(PREF_KEY_CREDITS, -1);
    }

    @Override
    public void setSchoolBiz(List<BriefSchoolBusiness> businesses) {
        String schoolBizStr = mGson.toJson(businesses);
        mSharedPreferences
                .edit()
                .putString(PREF_KEY_SCHOOL_BIZ, schoolBizStr)
                .apply();
    }

    @Override
    public List<BriefSchoolBusiness> getSchoolBiz() {
        String schoolBizStr = mSharedPreferences.getString(PREF_KEY_SCHOOL_BIZ, null);
        if (null != schoolBizStr) {
            return mGson.fromJson(schoolBizStr,
                    new TypeToken<List<BriefSchoolBusiness>>() {
                    }.getType());
        }
        return null;
    }

    @Override
    public void saveScanType(int scanType) {
        mSharedPreferences
                .edit()
                .putInt(PREF_KEY_SCAN_TYPE, scanType)
                .apply();
    }

    @Override
    public int getScanType() {
        return mSharedPreferences.getInt(PREF_KEY_SCAN_TYPE, BluetoothConstants.SCAN_TYPE_BLE);
    }

    @Override
    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    @Override
    public String getPushToken() {
        return this.pushToken;
    }

    @Override
    public void setPushTag(String pushTag) {
        mSharedPreferences
                .edit()
                .putString(PREF_KEY_PUSH_TAG, pushTag)
                .apply();

    }

    @Override
    public String getPushTag() {
        return mSharedPreferences.getString(PREF_KEY_PUSH_TAG, "");
    }

    @Override
    public void setBathPasswordDescription(ArrayList<String> bathPasswordDescription) {
        String schoolBizStr = mGson.toJson(bathPasswordDescription);
        mUnclearSharedPreferences
                .edit()
                .putString(PREF_KEY_BATH_ROOM_PASSWORD_DESC, schoolBizStr)
                .apply();
    }

    @Override
    public List<String> getBathPasswordDescription() {
        String schoolBizStr = mUnclearSharedPreferences.getString(PREF_KEY_BATH_ROOM_PASSWORD_DESC, null);
        if (null != schoolBizStr) {
            return mGson.fromJson(schoolBizStr,
                    new TypeToken<List<String>>() {
                    }.getType());
        }
        return null;
    }

    @Override
    public void setNormalBathroom(NormalBathroom normalBathroom) {

    }

    @Override
    public NormalBathroom getNormalBathroom() {
        return null;
    }

    @Override
    public void setBathroomPassword(String password) {
        mSharedPreferences
                .edit()
                .putString(PREF_KEY_BATH_ROOM_PASSWORD, password)
                .apply();
    }

    @Override
    public String getBathroomPassword() {
        return mSharedPreferences.getString(PREF_KEY_BATH_ROOM_PASSWORD ,"");
    }

    @Override
    public void setRoomId(Long id) {
        if (id != null){
            mSharedPreferences.edit().putLong(PREF_KEY_ROOMID , id).apply();
        }
    }

    @Override
    public Long getRoomId() {
        Long roomId = mSharedPreferences.getLong(PREF_KEY_ROOMID , -1L);
        return roomId ;
    }

    @Override
    public void setBookMethod(int bookMethod) {
        mSharedPreferences
                .edit()
                .putInt(PREF_KEY_BOOK_METHOD, bookMethod)
                .apply();
    }

    @Override
    public int getBookMethrod() {
        return mSharedPreferences.getInt(PREF_KEY_BOOK_METHOD , -1);
    }

    @Override
    public void setTopic(List<BbsTopicListTradeRespDTO.TopicListBean> topicListBeans) {
        String topic = mGson.toJson(topicListBeans);
        mSharedPreferences.edit().putString(PREF_KEY_SOCAL_TAG ,topic)
                .apply();
    }

    @Override
    public List<BbsTopicListTradeRespDTO.TopicListBean> getTopic() {
        String topic = mSharedPreferences.getString(PREF_KEY_SOCAL_TAG ,"");
        if (topic != null){
            return mGson.fromJson(topic ,new TypeToken<List<BbsTopicListTradeRespDTO.TopicListBean>>(){}.getType());
        }else {
            return null;
        }
    }

    @Override
    public void setCommentEnable(boolean commentEnable) {
        mSharedPreferences.edit().putBoolean(PREF_KEY_COMMENTABLE ,commentEnable).apply();
    }

    @Override
    public boolean getCommentEnable() {
        return mSharedPreferences.getBoolean(PREF_KEY_COMMENTABLE ,true);
    }

    @Override
    public void setIsFirstAfterLogin(boolean isFisrstAfterLogin) {
        mSharedPreferences.edit().putBoolean(PREF_KEY_ISFIRST_AFTER_LOGIN ,isFisrstAfterLogin).apply();
    }

    @Override
    public boolean getIsFirstAfterLogin() {
        return mSharedPreferences.getBoolean(PREF_KEY_ISFIRST_AFTER_LOGIN ,false);
    }

    @Override
    public void setCertifyStauts(int certifyStauts) {
        mSharedPreferences.edit().putInt(PREF_KEY_CERTIFY_STATUS ,-1).apply();
    }

    @Override
    public int getCertifyStatus() {
        return mSharedPreferences.getInt(PREF_KEY_CERTIFY_STATUS ,-1);
    }

    @Override
    public void setIsShowWithDrawDialog(boolean isShowWithDrawDialog ) {
         mSharedPreferences.edit().putBoolean(PREF_KEY_WITHDRAW  , isShowWithDrawDialog).apply();
    }

    @Override
    public boolean isShowWithDrawDialog() {
        return mSharedPreferences.getBoolean(PREF_KEY_WITHDRAW ,true);
    }


    @Override
    public void logout() {
        mSharedPreferences.edit().clear().apply();
        tokenHolder = null;
        userHolder = null;
        bonusAmount = 0;
        balance = "";
        showUrgencyNotify = true;
    }


    private boolean isUserHolderEmpty() {
        if (userHolder != null) {
            if (userHolder.getNickName() != null
                    && userHolder.getSchoolName() != null
                    && userHolder.getMobile() != null
                    && !TextUtils.isEmpty(userHolder.getDormitory())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setConnectCmd(String macAddress, String connectCmd) {
        mSharedPreferences.edit().putString(PREF_CMD_CONNECT_PREFIX + macAddress, connectCmd).apply();
    }

    @Override
    public String getConnectCmd(String macAddress) {
        return mSharedPreferences.getString(PREF_CMD_CONNECT_PREFIX + macAddress, null);
    }

    @Override
    public void setCloseCmd(String macAddress, String closeCmd) {
        mSharedPreferences.edit().putString(PREF_CMD_CLOSE_PREFIX + macAddress, closeCmd).apply();
    }

    @Override
    public String getCloseCmd(String macAddress) {
        return mSharedPreferences.getString(PREF_CMD_CLOSE_PREFIX + macAddress, null);
    }

    @Override
    public void setDeviceNoAndMacAddress(String deviceNo, String macAddress) {
        mSharedPreferences.edit().putString(PREF_BLUETOOTH_MAC_ADDRESS_PREFIX + deviceNo, macAddress).apply();
    }

    @Override
    public String getMacAddressByDeviceNo(String deviceNo) {
        return mSharedPreferences.getString(PREF_BLUETOOTH_MAC_ADDRESS_PREFIX + deviceNo, null);
    }
}
