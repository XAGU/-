package com.xiaolian.amigo.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;

/**
 * 未找零金额dialog
 *
 * @author zcd
 * @date 17/10/12
 */

public class ScanDialog extends Dialog {
    private TextView tvTip;
    private TextView tvTitle;
    private TextView tvCancel;
    private TextView tvOk;
    private OnOkClickListener okClickListener;
    private OnCancelClickListener cancelClickListener;

    public ScanDialog(@NonNull Context context ) {
        super(context, R.style.AlertDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        //此处可以设置dialog显示的位置
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_scan);
        tvTip = findViewById(R.id.tv_tip);
        tvCancel = findViewById(R.id.tv_cancel);
        tvOk = findViewById(R.id.tv_ok);
        tvCancel.setOnClickListener(v -> {
            if (cancelClickListener != null) {
                cancelClickListener.onCancelClick(this);
                dismiss();
            }
        });
        tvOk.setOnClickListener(v -> {
            if (okClickListener != null) {
                okClickListener.onOkClick(this);
                dismiss();
            }
        });
    }

    public void setCancelText(String text) {
        tvCancel.setText(text);
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
