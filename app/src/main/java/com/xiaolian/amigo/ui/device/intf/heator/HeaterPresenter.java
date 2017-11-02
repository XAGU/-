package com.xiaolian.amigo.ui.device.intf.heator;

import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IClientServiceDataManager;
import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.manager.intf.ITradeDataManager;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalWalletDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.device.DeviceBasePresenter;
import com.xiaolian.amigo.ui.device.WaterDeviceBasePresenter;

import javax.inject.Inject;

/**
 * Created by caidong on 2017/9/22.
 */
public class HeaterPresenter<V extends IHeaterView> extends WaterDeviceBasePresenter<V>
        implements IHeaterPresenter<V> {

    @Inject
    HeaterPresenter(IBleDataManager bleDataManager,
                    ITradeDataManager tradeDataManager,
                    IOrderDataManager orderDataManager,
                    IWalletDataManager walletDataManager,
                    IClientServiceDataManager clientServiceDataManager,
                    ISharedPreferencesHelp sharedPreferencesHelp) {
        super(bleDataManager, tradeDataManager, orderDataManager, walletDataManager, clientServiceDataManager, sharedPreferencesHelp);
    }

}
