package com.xiaolian.amigo.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.xiaolian.amigo.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * SharedPreferencesHelp实现类
 * @author zcd
 */
@Singleton
public class SharedPreferencesHelp implements ISharedPreferencesHelp {
    private static final String PREF_FILE_NAME = "amigo";
    private static final String PREF_KEY_TOKEN = "PREF_KEY_TOKEN";

    private String tokenHolder;

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
}
