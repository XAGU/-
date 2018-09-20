package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.ViewUtil;

/**
 * 供水时段、提现时段提示
 *
 * @author zcd
 * @date 17/9/25
 */

public class LostAndFoundReplyDialog extends Dialog {

    private Context context;
    private EditText etReply;
    private TextView tvSend;
    private OnPublishClickListener listener;

    public LostAndFoundReplyDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER|Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        this.context = context;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_lost_and_found_reply);
        tvSend = findViewById(R.id.tv_send);
        tvSend.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPublishClick(this, etReply.getText().toString());
            }
        });
        etReply = findViewById(R.id.et_reply);
        etReply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    tvSend.setEnabled(true);
                    tvSend.setTextColor(ContextCompat.getColor(context, R.color.white));
                } else {
                    tvSend.setEnabled(false);
                    tvSend.setTextColor(ContextCompat.getColor(context, R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
//        ViewUtil.setEditHintAndSize("请输入内容...", 14, etReply);
    }

    @Override
    public void show() {
        super.show();
        CommonUtil.showSoftInput(context, etReply);
    }

    public void setPublishClickListener(OnPublishClickListener listener) {
        this.listener = listener;
    }

    public void clearInput() {
        etReply.setText("");
    }

    public void setReplyUser(String replyToUserName) {
    }

    public interface OnPublishClickListener {
        void onPublishClick(Dialog dialog, String comment);
    }
}
