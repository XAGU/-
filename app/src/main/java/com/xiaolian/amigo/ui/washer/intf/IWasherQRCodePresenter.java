package com.xiaolian.amigo.ui.washer.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 洗衣机展示二维码
 * <p>
 * Created by zcd on 18/1/12.
 */

public interface IWasherQRCodePresenter<V extends IWasherQRCodeView> extends IBasePresenter<V> {
    void generateQRCode(String price, String mode, int dimension);
}
