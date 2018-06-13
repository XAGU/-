package com.xiaolian.amigo.ui.widget.dialog;

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
 * @author zcd
 * @date 18/6/13
 */
public class LostAndFoundPublishDialog extends Dialog {

    private PublishLostAndFoundListener publishLostAndFoundListener;
    private Context context;
    private TextView tvLost;
    private TextView tvFound;
    private TextView tvCancel;
    private View vOther;

    public LostAndFoundPublishDialog(@NonNull Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        this.context = context;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_lost_and_found_publish);
        tvLost = findViewById(R.id.tv_lost);
        tvLost.setOnClickListener(v -> {
            dismiss();
            if (publishLostAndFoundListener != null) {
                publishLostAndFoundListener.onPublishLost(this);
            }
        });
        tvFound = findViewById(R.id.tv_found);
        tvFound.setOnClickListener(v -> {
            dismiss();
            if (publishLostAndFoundListener != null) {
                publishLostAndFoundListener.onPublishFound(this);
            }
        });
        tvCancel = findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> {
            dismiss();
        });
        vOther = findViewById(R.id.v_other);
    }

    public void setOnPublishLostAndFoundListener(PublishLostAndFoundListener listener) {
        this.publishLostAndFoundListener = listener;
    }

    public interface PublishLostAndFoundListener {
        void onPublishLost(Dialog dialog);
        void onPublishFound(Dialog dialog);
    }
}
