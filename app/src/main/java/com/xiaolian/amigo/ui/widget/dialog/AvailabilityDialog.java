package com.xiaolian.amigo.ui.widget.dialog;

import android.app.AlertDialog;
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
 * 供水时段、提现时段提示
 * <p>
 * Created by zcd on 9/25/17.
 */

public class AvailabilityDialog extends Dialog {

    private TextView tv_tip;
    private TextView tv_sub_tip;
    private TextView tv_cancel;
    private TextView tv_ok;
    private OnOkClickListener listener;

    public AvailabilityDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_avaliability);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_sub_tip = (TextView) findViewById(R.id.tv_sub_tip);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(v -> dismiss());
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOkClick(this);
            }
            dismiss();
        });
    }

    public void setTip(String tip) {
        tv_tip.setText(tip);
    }

    public void setSubTipVisible(boolean visible) {
        tv_sub_tip.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setSubTip(String subTip) {
        tv_sub_tip.setText(subTip);
    }

    public void setOkText(String ok) {
        tv_ok.setText(ok);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }

    public interface OnOkClickListener{
        void onOkClick(Dialog dialog);
    }
}
