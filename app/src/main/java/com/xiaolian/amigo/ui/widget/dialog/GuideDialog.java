package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;

/**
 * <p>
 * Created by zcd on 17/11/6.
 */

public class GuideDialog extends Dialog {
    public static final int TYPE_MAIN = 1;
    public static final int TYPE_HEATER = 2;
    public static final int TYPE_DISPENER = 3;

    private ImageView iv_button;
    private TextView tv_location;

    public GuideDialog(@NonNull Context context, int type) {
        super(context, R.style.AlertDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        setCanceledOnTouchOutside(false);
        initView(type);
    }

    private void initView(int type) {
        switch (type) {
            case TYPE_MAIN:
                setContentView(R.layout.dialog_guide_main);
                break;
            case TYPE_HEATER:
                setContentView(R.layout.dialog_guide_heater);
                tv_location = (TextView) findViewById(R.id.tv_device_title);
                break;
            case TYPE_DISPENER:
                setContentView(R.layout.dialog_guide_dispenser);
                tv_location = (TextView) findViewById(R.id.tv_device_title);
                break;
        }
        iv_button = (ImageView) findViewById(R.id.iv_button);
        iv_button.setOnClickListener((v -> {
            dismiss();
        }));
    }

    public void setLocation(String location) {
        if (tv_location != null) {
            tv_location.setText(location);
        }
    }
}
