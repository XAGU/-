package com.xiaolian.amigo.ui.device.washer;

import android.graphics.Bitmap;

import com.google.zxing.WriterException;
import com.xiaolian.amigo.data.manager.intf.IWasherDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IWasherQrCodePresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IWasherQrCodeView;
import com.xiaolian.amigo.ui.widget.qrcode.QRCodeEncoder;

import javax.inject.Inject;

/**
 * 洗衣机展示二维码
 *
 * @author zcd
 * @date 18/1/12
 */

public class WasherQrCodePresenter<V extends IWasherQrCodeView> extends BasePresenter<V>
        implements IWasherQrCodePresenter<V> {
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private IWasherDataManager washerDataManager;

    @Inject
    WasherQrCodePresenter(IWasherDataManager washerDataManager) {
        this.washerDataManager = washerDataManager;
    }

    @Override
    public void generateQRCode(String data, int dimension) {
        try {
            Bitmap bitmap = QRCodeEncoder.encodeAsBitmap(data, dimension);
            getMvpView().renderQRCode(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            getMvpView().onError("生成二维码失败");
        }
    }
}
