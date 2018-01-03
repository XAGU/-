package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
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
    private ImageView iv_symbol;
    private TextView tv_title;
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
        bt_ok = findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOkClick(this, isNotReminder);
            }
            dismiss();
        });
        tv_title = findViewById(R.id.tv_title);
        iv_symbol = findViewById(R.id.iv_symbol);
        tv_content = findViewById(R.id.tv_content);
        iv_not_reminder = findViewById(R.id.iv_not_remind);
        ll_not_reminder = findViewById(R.id.ll_not_remind);
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

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setTitle(int titleRes) {
        tv_title.setText(titleRes);
    }

    public void setContent(String content) {
        tv_content.setText(content);
    }

    public void setContent(Spanned content) {
        tv_content.setText(content);
    }

    public void setContent(int contentRes) {
        tv_content.setText(contentRes);
    }

    public void hideNoticeSymbol() {
        iv_symbol.setVisibility(View.GONE);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }

    public interface OnOkClickListener{
        void onOkClick(Dialog dialog, boolean isNotReminder);
    }

}
