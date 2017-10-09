package com.xiaolian.amigo.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * SharedPreferencesHelp实现类
 * @author zcd
 */
public class SharedPreferencesHelp implements ISharedPreferencesHelp {
    private static final String PREF_FILE_NAME = "amigo";
    private static final String PREF_KEY_TOKEN = "PREF_KEY_TOKEN";
    private static final String PREF_KEY_RESIDENCEID = "PREF_KEY_RESIDENCEID";
    private static final String PREF_KEY_SCHOOLID = "PREF_KEY_SCHOOLID";
    private static final String PREF_KEY_SCHOOLNAME = "PREF_KEY_SCHOOLNAME";
    private static final String PREF_KEY_NICKNAME = "PREF_KEY_NICKNAME";
    private static final String PREF_KEY_MOBILE = "PREF_KEY_MOBILE";

    private String tokenHolder;
    private User userHolder;

    private boolean isShowUrgencyNotify = true;
    private int bonusAmount = 0;

    private final SharedPreferences mSharedPreferences;

    @Inject
    public SharedPreferencesHelp(@ApplicationContext Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
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
        mSharedPreferences.edit().putString(PREF_KEY_TOKEN, token).apply();
    }

    @Override
    public User getUserInfo() {
        if (!isUserHolderEmpty()) {
            return userHolder;
        }
        if (userHolder == null) {
            userHolder = new User();
        }
        userHolder.setResidenceId(mSharedPreferences.getLong(PREF_KEY_RESIDENCEID, -1));
        userHolder.setSchoolId(mSharedPreferences.getLong(PREF_KEY_SCHOOLID, -1));
        userHolder.setSchoolName(mSharedPreferences.getString(PREF_KEY_SCHOOLNAME, null));
        userHolder.setNickName(mSharedPreferences.getString(PREF_KEY_NICKNAME, null));
        userHolder.setMobile(mSharedPreferences.getString(PREF_KEY_MOBILE, null));
        return userHolder;
    }

    @Override
    public void setUserInfo(User user) {
        userHolder = user;
        if(null != user.getResidenceId()) {
            mSharedPreferences.edit().putLong(PREF_KEY_RESIDENCEID, user.getResidenceId()).apply();
        }
        mSharedPreferences.edit().putLong(PREF_KEY_SCHOOLID, user.getSchoolId()).apply();
        mSharedPreferences.edit().putString(PREF_KEY_SCHOOLNAME, user.getSchoolName()).apply();
        mSharedPreferences.edit().putString(PREF_KEY_NICKNAME, user.getNickName()).apply();
        mSharedPreferences.edit().putString(PREF_KEY_MOBILE, user.getMobile()).apply();
    }

    @Override
    public boolean isShowUrgencyNotify() {
        return this.isShowUrgencyNotify;
    }

    @Override
    public void setShowUrgencyNotify(boolean isShow) {
        this.isShowUrgencyNotify = isShow;
    }

    @Override
    public void setBonusAmount(int amount) {
        this.bonusAmount = amount;
    }

    @Override
    public int getBonusAmount() {
        return this.bonusAmount;
    }

    @Override
    public void logout() {
        mSharedPreferences.edit().clear().apply();
        tokenHolder = null;
        userHolder = null;
        bonusAmount = 0;
        isShowUrgencyNotify = true;
    }


    boolean isUserHolderEmpty() {
        if (userHolder != null) {
            if (userHolder.getNickName() != null
                    && userHolder.getSchoolName() != null
                    && userHolder.getMobile() != null) {
                return false;
            }
        }
        return true;
    }
}
