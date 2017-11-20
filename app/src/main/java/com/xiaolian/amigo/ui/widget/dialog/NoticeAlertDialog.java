package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;

/**
 * 通知dialog
 * <p>
 * Created by zcd on 9/25/17.
 */

public class NoticeAlertDialog extends Dialog {

    private LinearLayout ll_not_reminder;
    private ImageView iv_not_reminder;
    private Button bt_ok;
    private TextView tv_content;
    private OnOkClickListener listener;
    private boolean isNotReminder;
    public NoticeAlertDialog(@NonNull Context context) {
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
        setContentView(R.layout.dialog_notice);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOkClick(this, isNotReminder);
            }
            dismiss();
        });
        tv_content = (TextView) findViewById(R.id.tv_content);
        iv_not_reminder = (ImageView) findViewById(R.id.iv_not_remind);
        ll_not_reminder = (LinearLayout) findViewById(R.id.ll_not_remind);
        ll_not_reminder.setOnClickListener(v -> {
            if (isNotReminder) {
                isNotReminder = false;
                iv_not_reminder.setImageResource(R.drawable.dot_gray);
            } else {
                isNotReminder = true;
                iv_not_reminder.setImageResource(R.drawable.dot_red);
            }
        });
    }

    public void setContent(String content) {
        tv_content.setText(content);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }

    public interface OnOkClickListener{
        void onOkClick(Dialog dialog, boolean isNotReminder);
    }

}
