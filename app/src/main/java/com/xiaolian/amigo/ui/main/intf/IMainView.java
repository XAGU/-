package com.xiaolian.amigo.ui.main.intf;

import android.content.Intent;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public interface IMainView extends IBaseView {
    void showNoticeAmount(Integer amount);

    void showTimeValidDialog(String title, String remark, Class clz, int deviceType);

    void gotoDevice(Class clz);

    void gotoDevice(Class clz, String macAddress);

    void showUrgentNotify(String content, Long id);

    void refreshNoticeAmount();
}
