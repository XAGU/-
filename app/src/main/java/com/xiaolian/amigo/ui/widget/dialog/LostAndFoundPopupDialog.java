package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ScreenUtils;

import lombok.NonNull;

/**
 * @author zcd
 * @date 18/5/13
 */
public class LostAndFoundPopupDialog extends Dialog {
    private Context context;
    private OnLostAndFoundClickListener listener;

    public LostAndFoundPopupDialog(@NonNull Context context) {
        super(context, R.style.LostAndFoundPopupDialogStyle);
        this.context = context;
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.END | Gravity.TOP);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.y = ScreenUtils.dpToPxInt(context, 45);
        lp.x = 24;
        window.setAttributes(lp);
        this.context = context;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_lost_and_found);
        TextView tvPublishLost = findViewById(R.id.tv_publish_lost);
        TextView tvPublishFound = findViewById(R.id.tv_publish_found);
        TextView tvMyPublish = findViewById(R.id.tv_my_publish);
        tvPublishLost.setOnClickListener(v -> {
            listener.onPublishLostClick();
            dismiss();
        });
        tvPublishFound.setOnClickListener(v -> {
            listener.onPublishFoundClick();
            dismiss();
        });
        tvMyPublish.setOnClickListener(v -> {
            listener.onMyPublishClick();
            dismiss();
        });
    }

    public void setLostAndFoundListener(OnLostAndFoundClickListener listener) {
        this.listener = listener;
    }

    public interface OnLostAndFoundClickListener {
        void onPublishLostClick();

        void onPublishFoundClick();

        void onMyPublishClick();
    }

    public void show(int x, int y) {

        super.show();
    }
}
