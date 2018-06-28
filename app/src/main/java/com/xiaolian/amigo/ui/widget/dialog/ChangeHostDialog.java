package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaolian.amigo.BuildConfig;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ViewUtil;

/**
 * 修改密码提示dialog
 *
 * @author zcd
 * @date 17/9/25
 */

public class ChangeHostDialog extends Dialog {

    private TextView tvOk;
    private TextView tvCancel;
    private EditText etHost;
    private EditText etHost2;
    private OnOkClickListener listener;

    public ChangeHostDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        initView(context);
    }

    private void initView(Context context) {
        setContentView(R.layout.dialog_changehost);
        tvOk = findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etHost.getText())) {
                Toast.makeText(context, "请输入host", Toast.LENGTH_SHORT).show();
                return;
            }
            if (listener != null) {
                listener.onOkClick(this, etHost.getText().toString(),
                        etHost2.getText().toString());
            }
            dismiss();
        });
        tvCancel = findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> dismiss());
        etHost = findViewById(R.id.et_host);
        etHost.setTypeface(null, Typeface.NORMAL);
        etHost.setText(BuildConfig.SERVER);
        etHost2 = findViewById(R.id.et_host2);
        etHost2.setTypeface(null, Typeface.NORMAL);
        etHost2.setText(BuildConfig.H5_SERVER);
        ViewUtil.setEditHintAndSize("请输入server", 14, etHost);
        ViewUtil.setEditHintAndSize("请输入h5 server", 14, etHost2);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }

    public interface OnOkClickListener {
        void onOkClick(Dialog dialog, String host, String host2);
    }
}
