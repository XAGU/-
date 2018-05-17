package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaolian.amigo.R;

/**
 * @author zcd
 * @date 18/5/17
 */

public class LostAndFoundBottomDialog extends Dialog {

    private OnOkClickListener listener;
    private Context context;
    private TextView tvOk;
    private TextView tvCancel;

    public LostAndFoundBottomDialog(@NonNull Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        this.context = context;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_lost_and_found_bottom);
        tvOk = findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onOkClick(this);
            }
        });
        tvCancel = findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> {
            dismiss();
        });
    }

    public void setOkText(String ok) {
        tvOk.setText(ok);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }

    public interface OnOkClickListener {
        void onOkClick(Dialog dialog);
    }
}
