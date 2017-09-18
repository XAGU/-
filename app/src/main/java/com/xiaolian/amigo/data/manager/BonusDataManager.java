package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IBonusDataManager;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * 红包DataManager实现类
 * @author zcd
 */

public class BonusDataManager implements IBonusDataManager {
    @Inject
    public BonusDataManager(Retrofit retrofit) {
    }
}
