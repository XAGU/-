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

    private String tokenHolder;
    private User userHolder;

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
        if (userHolder != null) {
            return userHolder;
        }
        userHolder = new User();
        userHolder.setResidenceId(mSharedPreferences.getInt(PREF_KEY_RESIDENCEID, -1));
        userHolder.setSchoolId(mSharedPreferences.getInt(PREF_KEY_SCHOOLID, -1));
        return userHolder;
    }

    @Override
    public void setUserInfo(User user) {
        userHolder = user;
        mSharedPreferences.edit().putInt(PREF_KEY_RESIDENCEID, user.getResidenceId()).apply();
        mSharedPreferences.edit().putInt(PREF_KEY_SCHOOLID, user.getSchoolId()).apply();
    }
}
