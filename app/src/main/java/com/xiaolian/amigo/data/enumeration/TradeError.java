package com.xiaolian.amigo.data.enumeration;

import com.xiaolian.amigo.R;

/**
 * Created by caidong on 2017/10/13.
 */
public enum TradeError {
    CONNECT_ERROR_1(true, R.string.connect_error_title_1, R.string.connect_error_tip_1, R.string.connect_error_btn, ErrorTag.CONNECT_ERROR.getCode()),
    CONNECT_ERROR_2(false, R.string.connect_error_title_2, R.string.connect_error_tip_2, R.string.connect_error_btn, ErrorTag.CONNECT_ERROR.getCode()),
    CONNECT_ERROR_3(true, R.string.connect_error_title_1, R.string.connect_error_tip_3, R.string.connect_error_btn, ErrorTag.CONNECT_ERROR.getCode()),
    CONNECT_ERROR_4(true, R.string.connect_error_title_1, R.string.connect_error_tip_4, R.string.connect_error_btn, ErrorTag.CONNECT_ERROR.getCode()),
    CONNECT_ERROR_5(true, R.string.connect_error_title_1, R.string.connect_error_tip_5, R.string.connect_error_btn, ErrorTag.CONNECT_ERROR.getCode()),
    DEVICE_BUSY(false, R.string.device_busy__title, R.string.device_busy_tip, R.string.device_busy_btn, ErrorTag.DEVICE_BUSY.getCode()),
    DEVICE_BROKEN_1(false, R.string.device_broken_title, R.string.device_broken_tip_1, R.string.device_broken_btn_1, ErrorTag.CALL.getCode()),
    DEVICE_BROKEN_2(false, R.string.device_broken_title, R.string.device_broken_tip_2, R.string.device_broken_btn_2, ErrorTag.REPAIR.getCode()),
    SYSTEM_ERROR(false, R.string.system_error_title, R.string.system_error_tip, R.string.device_broken_btn_1, ErrorTag.CALL.getCode());

    // 是否现在加载小圆点
    private boolean showLoading;

    // 错误标题
    private int errorTitle;

    // 错误提示
    private int errorTip;

    // 按钮内容
    private int btnText;

    // 按钮标签值 1-重新连接，2-切换宿舍，3-联系客服，4-前往报修
    private int btnTag;

    TradeError(boolean showLoading, int errorTitle, int errorTip, int btnText, int btnTag) {
        this.showLoading = showLoading;
        this.errorTitle = errorTitle;
        this.errorTip = errorTip;
        this.btnText = btnText;
        this.btnTag = btnTag;
    }

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
    }

    public int getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(int errorTitle) {
        this.errorTitle = errorTitle;
    }

    public int getErrorTip() {
        return errorTip;
    }

    public void setErrorTip(int errorTip) {
        this.errorTip = errorTip;
    }

    public int getBtnText() {
        return btnText;
    }

    public void setBtnText(int btnText) {
        this.btnText = btnText;
    }

    public int getBtnTag() {
        return btnTag;
    }

    public void setBtnTag(int btnTag) {
        this.btnTag = btnTag;
    }
}
