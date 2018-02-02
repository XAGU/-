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
 *
 * @author zcd
 * @date 17/9/25
 */

public class NoticeAlertDialog extends Dialog {

    private LinearLayout llNotReminder;
    private ImageView ivNotReminder;
    private ImageView ivSymbol;
    private TextView tvTitle;
    private Button btOk;
    private TextView tvContent;
    private OnOkClickListener listener;
    private boolean isNotReminder;

    public NoticeAlertDialog(@NonNull Context context) {
        super(context, R.style.NoticeAlertDialogStyle);
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
        btOk = findViewById(R.id.bt_ok);
        btOk.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOkClick(this, isNotReminder);
            }
            dismiss();
        });
        tvTitle = findViewById(R.id.tv_title);
        ivSymbol = findViewById(R.id.iv_symbol);
        tvContent = findViewById(R.id.tv_content);
        ivNotReminder = findViewById(R.id.iv_not_remind);
        llNotReminder = findViewById(R.id.ll_not_remind);
        llNotReminder.setOnClickListener(v -> {
            if (isNotReminder) {
                isNotReminder = false;
                ivNotReminder.setImageResource(R.drawable.dot_gray);
            } else {
                isNotReminder = true;
                ivNotReminder.setImageResource(R.drawable.dot_red);
            }
        });
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(int titleRes) {
        tvTitle.setText(titleRes);
    }

    public void setContent(String content) {
        tvContent.setText(content);
    }

    public void setContent(Spanned content) {
        tvContent.setText(content);
    }

    public void setContent(int contentRes) {
        tvContent.setText(contentRes);
    }

    public void hideNoticeSymbol() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tvTitle.getLayoutParams();
        lp.topMargin = 108;
        tvTitle.setLayoutParams(lp);
        ivSymbol.setVisibility(View.GONE);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }

    public interface OnOkClickListener {
        void onOkClick(Dialog dialog, boolean isNotReminder);
    }

}
