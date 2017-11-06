package com.xiaolian.amigo.ui.device.dispenser;

import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IClientServiceDataManager;
import com.xiaolian.amigo.data.manager.intf.IFavoriteManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.manager.intf.ITradeDataManager;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;
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
    private IFavoriteManager favoriteManager;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public DispenserPresenter(IBleDataManager bleDataManager,
                              ITradeDataManager tradeDataManager,
                              IOrderDataManager orderDataManager,
                              IWalletDataManager walletDataManager,
                              IClientServiceDataManager clientServiceDataManager,
                              ISharedPreferencesHelp sharedPreferencesHelp,
                              IFavoriteManager favoriteManager) {
        super(bleDataManager, tradeDataManager, orderDataManager, walletDataManager, clientServiceDataManager, sharedPreferencesHelp);
        this.favoriteManager = favoriteManager;
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public void onAttach(V view) {
        super.onAttach(view);
        if (!sharedPreferencesHelp.isDispenserGuideDone()) {
            getMvpView().showGuide();
            sharedPreferencesHelp.doneDispenserGuide();
        }
    }

    @Override
    public void favorite(Long id) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(id);
        addObserver(favoriteManager.favorite(reqDTO), new NetworkObserver<ApiResult<SimpleRespDTO>>() {

            @Override
            public void onReady(ApiResult<SimpleRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess("收藏成功");
                    getMvpView().setFavoriteIcon();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void unFavorite(Long id) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(id);
        addObserver(favoriteManager.unFavorite(reqDTO), new NetworkObserver<ApiResult<SimpleRespDTO>>() {

            @Override
            public void onReady(ApiResult<SimpleRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess("取消收藏成功");
                    getMvpView().setUnFavoriteIcon();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
