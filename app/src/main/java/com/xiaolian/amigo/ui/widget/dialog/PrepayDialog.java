package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.util.DensityUtil;

/**
 * 未找零金额dialog
 * <p>
 * Created by zcd on 10/12/17.
 */

public class PrepayDialog extends Dialog {
    private TextView tv_tip;
    private TextView tv_title;
    private TextView tv_cancel;
    private TextView tv_ok;
    private OnOkClickListener okClickListener;
    private OnCancelClickListener cancelClickListener;
    public PrepayDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_prepay);
        tv_title = findViewById(R.id.tv_title);
        tv_tip = findViewById(R.id.tv_tip);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_ok = findViewById(R.id.tv_ok);
        tv_cancel.setOnClickListener(v -> {
            if (cancelClickListener != null) {
                cancelClickListener.onCancelClick(this);
                dismiss();
            }
        });
        tv_ok.setOnClickListener(v -> {
            if (okClickListener != null) {
                okClickListener.onOkClick(this);
                dismiss();
            }
        });
    }

    public void setDeviceTypeAndPrepaySize(int type, int size) {
        String tip1 = "你在\"";
        String tip2 = "\"中有预付金额还未找零";
        String deviceStr = Device.getDevice(type).getDesc();
        SpannableString builder = new SpannableString(tip1 + deviceStr + tip2);
        tv_tip.setText(builder);
        tv_title.setText("你有" + size + "笔未找零金额！");
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.okClickListener = listener;
    }

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        this.cancelClickListener = listener;
    }
    public interface OnOkClickListener {
        void onOkClick(Dialog dialog);
    }
    public interface OnCancelClickListener {
        void onCancelClick(Dialog dialog);
    }

}
