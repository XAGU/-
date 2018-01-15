package com.xiaolian.amigo.ui.washer.intf;

import android.graphics.Bitmap;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 洗衣机展示二维码页面
 * <p>
 * Created by zcd on 18/1/12.
 */

public interface IWasherQRCodeView extends IBaseView {
    void renderQRCode(Bitmap bitmap);
}
