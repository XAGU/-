package com.xiaolian.amigo.ui.device.washer.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 扫描二维码
 *
 * @author zcd
 * @date 18/1/17
 */

public interface IScanPresenter<V extends IScanView> extends IBasePresenter<V> {
    /**
     * 将扫描到的二维码内容提交到服务器，进行预结账
     * @param content 二维码内容
     */
    void scanCheckout(String content);
}
