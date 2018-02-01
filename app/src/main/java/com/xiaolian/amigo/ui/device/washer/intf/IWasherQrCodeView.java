package com.xiaolian.amigo.ui.device.washer.intf;

import android.graphics.Bitmap;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 洗衣机展示二维码页面
 *
 * @author zcd
 * @date 18/1/12
 */

public interface IWasherQrCodeView extends IBaseView {
    /**
     * 显示二维码
     *
     * @param bitmap 二维码图片
     */
    void renderQRCode(Bitmap bitmap);
}
