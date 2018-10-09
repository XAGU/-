package com.xiaolian.amigo.ui.user.intf;

import android.widget.TextView;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

public interface IFindBathroomPasswordView extends IBaseView{

    /**
     * 跳转到下一步
     */
    void nextStep();

    /**
     * 开始倒计时
     */
    void startTimer();

    /**
     * 返回到个人中心
     */
    void backToCenter();
}
