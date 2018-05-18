package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.CommonUtil;

/**
 * 供水时段、提现时段提示
 *
 * @author zcd
 * @date 17/9/25
 */

public class LostAndFoundCommentDialog extends Dialog {

    private Context context;
    private EditText etComment;
    private TextView tvSend;
    private OnPublishClickListener listener;

    public LostAndFoundCommentDialog(@NonNull Context context) {
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
        setContentView(R.layout.dialog_lost_and_found_comment);
        tvSend = findViewById(R.id.tv_send);
        tvSend.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPublishClick(this, etComment.getText().toString());
            }
        });
        etComment = findViewById(R.id.et_comment);
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    tvSend.setEnabled(true);
                    tvSend.setTextColor(ContextCompat.getColor(context, R.color.lost_and_found_blue));
                } else {
                    tvSend.setEnabled(false);
                    tvSend.setTextColor(ContextCompat.getColor(context, R.color.colorDarkB));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void show() {
        super.show();
        CommonUtil.showSoftInput(context, etComment);
    }

    public void setPublishClickListener(OnPublishClickListener listener) {
        this.listener = listener;
    }

    public void clearInput() {
        etComment.setText("");
    }

    public interface OnPublishClickListener {
        void onPublishClick(Dialog dialog, String comment);
    }
}
