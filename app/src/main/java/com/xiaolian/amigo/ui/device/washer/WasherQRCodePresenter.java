package com.xiaolian.amigo.ui.device.washer;

import android.graphics.Bitmap;

import com.google.zxing.WriterException;
import com.xiaolian.amigo.data.manager.intf.IWasherDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IWasherQRCodePresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IWasherQRCodeView;
import com.xiaolian.amigo.ui.widget.qrcode.QRCodeEncoder;

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
