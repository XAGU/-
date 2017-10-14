package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.DensityUtil;

/**
 * 未找零金额dialog
 * <p>
 * Created by zcd on 10/12/17.
 */

public class PrepayDialog extends Dialog {
    TextView tv_tip;
    TextView tv_cancel;
    TextView tv_ok;
    int type;
    int prepaySize = 0;
    Context context;
    private OnOkClickListener okClickListener;
    private OnCancelClickListener cancelClickListener;
    public PrepayDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle);
        this.context = context;
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
        setContentView(R.layout.dialog_prepay);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_cancel.setOnClickListener(v -> {
            if (cancelClickListener != null) {
                cancelClickListener.onCancelClick(this);
                dismiss();
            }
        });
        tv_ok.setOnClickListener(v -> {
            if (okClickListener != null) {
                okClickListener.onOkClick(this);
                dismiss();
            }
        });
    }

    public void setDeviceTypeAndPrepaySize(int type, int size) {
        this.type = type;
        prepaySize = size;

        String tip1 = "你在\"";
        String tip2 = "\"中有";
        String tip3 = "笔未找零的金额";
        String deviceStr = "未知设备";
        if (type == 1) {
            deviceStr = "热水澡";
        } else if (type == 2) {
            deviceStr = "饮水机";
        }
        SpannableString builder = new SpannableString(tip1 + deviceStr + tip2
                + String.valueOf(prepaySize) + tip3);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#ff5555")),
                tip1.length() + deviceStr.length() + tip2.length(),
                tip1.length() + deviceStr.length() + tip2.length() + String.valueOf(prepaySize).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new AbsoluteSizeSpan(DensityUtil.dp2px(22)),
                tip1.length() + deviceStr.length() + tip2.length(),
                tip1.length() + deviceStr.length() + tip2.length() + String.valueOf(prepaySize).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_tip.setText(builder);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.okClickListener = listener;
    }

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        this.cancelClickListener = listener;
    }
    public interface OnOkClickListener {
        void onOkClick(Dialog dialog);
    }
    public interface OnCancelClickListener {
        void onCancelClick(Dialog dialog);
    }

}
