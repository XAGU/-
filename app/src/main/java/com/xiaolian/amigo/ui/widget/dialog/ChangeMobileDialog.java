package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ViewUtil;

/**
 * 修改密码提示dialog
 * <p>
 * Created by zcd on 9/25/17.
 */

public class ChangeMobileDialog extends Dialog {

    private TextView tv_ok;
    private TextView tv_cancel;
    private EditText et_password;
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
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(v -> {
            if (TextUtils.isEmpty(et_password.getText())) {
                Toast.makeText(context, context.getString(R.string.password_hint), Toast.LENGTH_SHORT).show();
                return;
            }
            if (listener != null) {
                listener.onOkClick(this, et_password.getText().toString());
            }
            dismiss();
        });
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(v -> dismiss());
        et_password = (EditText) findViewById(R.id.et_password);
        ViewUtil.setEditHintAndSize(context.getString(R.string.password_hint), 14, et_password);

    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }

    public interface OnOkClickListener{
        void onOkClick(Dialog dialog, String password);
    }
}
