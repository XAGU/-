package com.xiaolian.amigo.ui.device.dispenser;

import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.manager.intf.ITradeDataManager;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.device.WaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.dispenser.IDispenserPresenter;
import com.xiaolian.amigo.ui.device.intf.dispenser.IDispenserView;

import javax.inject.Inject;

/**
 * <p>
 * Created by zcd on 10/13/17.
 */

public class DispenserPresenter<V extends IDispenserView> extends WaterDeviceBasePresenter<V>
        implements IDispenserPresenter<V> {

    @Inject
    public DispenserPresenter(IBleDataManager bleDataManager, ITradeDataManager tradeDataManager, IOrderDataManager orderDataManager, IWalletDataManager walletDataManager, ISharedPreferencesHelp sharedPreferencesHelp) {
        super(bleDataManager, tradeDataManager, orderDataManager, walletDataManager, sharedPreferencesHelp);
    }
}
