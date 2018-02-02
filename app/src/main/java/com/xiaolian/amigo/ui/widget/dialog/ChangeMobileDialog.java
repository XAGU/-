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

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ViewUtil;

/**
 * 修改密码提示dialog
 *
 * @author zcd
 * @date 17/9/25
 */

public class ChangeMobileDialog extends Dialog {

    private TextView tvOk;
    private TextView tvCancel;
    private EditText etPassword;
    private OnOkClickListener listener;

    public ChangeMobileDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        initView(context);
    }

    private void initView(Context context) {
        setContentView(R.layout.dialog_changepassword);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etPassword.getText())) {
                Toast.makeText(context, context.getString(R.string.password_hint), Toast.LENGTH_SHORT).show();
                return;
            }
            if (listener != null) {
                listener.onOkClick(this, etPassword.getText().toString());
            }
            dismiss();
        });
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> dismiss());
        etPassword = (EditText) findViewById(R.id.et_password);
        etPassword.setTypeface(null, Typeface.NORMAL);
        ViewUtil.setEditHintAndSize(context.getString(R.string.password_hint), 14, etPassword);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }

    public interface OnOkClickListener {
        void onOkClick(Dialog dialog, String password);
    }
}
