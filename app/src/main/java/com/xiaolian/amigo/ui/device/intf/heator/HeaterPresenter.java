package com.xiaolian.amigo.ui.device.intf.heator;

import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IClientServiceDataManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.manager.intf.ITradeDataManager;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.device.WaterDeviceBasePresenter;

import javax.inject.Inject;

/**
 * Created by caidong on 2017/9/22.
 */
public class HeaterPresenter<V extends IHeaterView> extends WaterDeviceBasePresenter<V>
        implements IHeaterPresenter<V> {

    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    HeaterPresenter(IBleDataManager bleDataManager,
                    ITradeDataManager tradeDataManager,
                    IOrderDataManager orderDataManager,
                    IWalletDataManager walletDataManager,
                    IClientServiceDataManager clientServiceDataManager,
                    ISharedPreferencesHelp sharedPreferencesHelp) {
        super(bleDataManager, tradeDataManager, orderDataManager, walletDataManager, clientServiceDataManager, sharedPreferencesHelp);
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public void onAttach(V view) {
        super.onAttach(view);
        if (!sharedPreferencesHelp.isHeaterGuideDone()) {
            getMvpView().showGuide();
            sharedPreferencesHelp.doneHeaterGuide();
        }
    }

}
