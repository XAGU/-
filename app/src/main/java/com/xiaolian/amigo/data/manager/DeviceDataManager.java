package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * 设备数据管理
 * <p>
 * Created by zcd on 9/29/17.
 */

public class DeviceDataManager implements IDeviceDataManager {
    private static final String TAG = DeviceDataManager.class.getSimpleName();

    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public DeviceDataManager(Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        this.sharedPreferencesHelp  = sharedPreferencesHelp;
    }
    @Override
    public void setBonusAmount(int amount) {
        sharedPreferencesHelp.setBonusAmount(amount);
    }

    @Override
    public int getBonusAmount() {
        return sharedPreferencesHelp.getBonusAmount();
    }
}
