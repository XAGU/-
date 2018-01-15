package com.xiaolian.amigo.ui.washer;

import com.xiaolian.amigo.data.manager.intf.IWasherDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.washer.intf.IWasherQRCodePresenter;
import com.xiaolian.amigo.ui.washer.intf.IWasherQRCodeView;

import javax.inject.Inject;

/**
 * 洗衣机展示二维码
 * <p>
 * Created by zcd on 18/1/12.
 */

public class WasherQRCodePresenter<V extends IWasherQRCodeView> extends BasePresenter<V>
        implements IWasherQRCodePresenter<V>{
    private IWasherDataManager washerDataManager;

    @Inject
    WasherQRCodePresenter(IWasherDataManager washerDataManager) {
        this.washerDataManager = washerDataManager;
    }

    @Override
    public void generateQRCode(String price, String mode) {

    }
}
