package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.ICreditsDataManager;
import com.xiaolian.amigo.data.network.ICreditsApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.credits.CreditsExchangeReqDTO;
import com.xiaolian.amigo.data.network.model.credits.CreditsRuleRespDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 积分模块
 *
 * @author zcd
 * @date 18/2/26
 */

public class CreditsDataManager implements ICreditsDataManager {
    private ICreditsApi creditsApi;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public CreditsDataManager(Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        this.creditsApi = retrofit.create(ICreditsApi.class);
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public Observable<ApiResult<CreditsRuleRespDTO>> getRules() {
        return creditsApi.getRules();
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> exchange(CreditsExchangeReqDTO reqDTO) {
        return creditsApi.exchange(reqDTO);
    }

    @Override
    public int getCredits() {
        return sharedPreferencesHelp.getCredits();
    }

    @Override
    public void setCredits(int credits) {
        sharedPreferencesHelp.setCredits(credits);
    }
}
