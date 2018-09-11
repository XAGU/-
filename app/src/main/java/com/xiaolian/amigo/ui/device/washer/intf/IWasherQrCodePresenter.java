package com.xiaolian.amigo.ui.device.washer.intf;

import android.content.Context;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 洗衣机展示二维码
 *
 * @author zcd
 * @date 18/1/12
 */
public interface IWasherQrCodePresenter<V extends IWasherQrCodeView> extends IBasePresenter<V> {
    /**
     * 生成二维码
     *
     * @param data      二维码内容
     * @param dimension 二维码尺寸
     */
    void generateQRCode(String data, int dimension , Context context);
}
