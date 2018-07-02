package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

    private OnOkClickListener onOkClickListener;
    private OnOtherClickListener onOtherClickListener;
    private Context context;
    private TextView tvOk;
    private TextView tvCancel;
    private TextView tvOther;
    private View vOther;

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
            if (onOkClickListener != null) {
                onOkClickListener.onOkClick(this);
            }
        });
        tvCancel = findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> {
            dismiss();
        });
        tvOther = findViewById(R.id.tv_other);
        tvOther.setOnClickListener(v -> {
            dismiss();
            if (onOtherClickListener != null) {
                onOtherClickListener.onOtherClick(this);
            }
        });
        vOther = findViewById(R.id.v_other);
    }

    public void setOkText(String ok) {
        tvOk.setText(ok);
        tvOther.setVisibility(View.GONE);
        vOther.setVisibility(View.GONE);
    }

    public void setOkTextColor(int colorRes) {
        tvOk.setTextColor(ContextCompat.getColor(context, colorRes));
    }

    public void setOtherTextColor(int colorRes) {
        tvOther.setTextColor(ContextCompat.getColor(context, colorRes));
    }

    public void setOtherText(String otherText) {
        tvOther.setText(otherText);
        tvOther.setVisibility(View.VISIBLE);
        vOther.setVisibility(View.VISIBLE);
    }

    public void hideOtherText() {
        tvOther.setVisibility(View.GONE);
        vOther.setVisibility(View.GONE);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.onOkClickListener = listener;
    }

    public void setOnOtherClickListener(OnOtherClickListener listener) {
        this.onOtherClickListener = listener;
    }

    public interface OnOtherClickListener {
        void onOtherClick(Dialog dialog);
    }

    public interface OnOkClickListener {
        void onOkClick(Dialog dialog);
    }
}
