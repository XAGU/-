package com.xiaolian.amigo.data;

import android.content.Context;

import com.xiaolian.amigo.data.network.ILoginService;

import javax.inject.Inject;

/**
 * Created by adamzfc on 9/14/17.
 */

public class AppDataManager {

    private Context mContext;
    private ILoginService mService;

    @Inject
    public AppDataManager(Context context,
                          ILoginService service) {
        mContext = context;
        mService = service;
    }

}
